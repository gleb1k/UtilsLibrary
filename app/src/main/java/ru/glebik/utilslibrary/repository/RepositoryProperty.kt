package ru.glebik.utilslibrary.repository

import ru.glebik.utilslibrary.repository.sources.CachedSource
import ru.glebik.utilslibrary.repository.sources.InMemoryCachedSource
import ru.glebik.utilslibrary.repository.sources.NoParamCachedSource
import ru.glebik.utilslibrary.repository.sources.NoParamRemoteSource
import ru.glebik.utilslibrary.repository.sources.RemoteSource


typealias NoParamRepositoryProperty<T> = RepositoryProperty<T, Parameters.NONE>

fun <T> noParamInMemoryRepositoryProperty(
    remoteSource: suspend () -> T
): RepositoryProperty<T, Parameters.NONE> = repositoryProperty(
    remoteSource = NoParamRemoteSource(remoteSource),
    cachedSource = NoParamCachedSource()
)

fun <T, Param : Parameters> inMemoryRepositoryProperty(
    remoteSource: RemoteSource<T, Param>
): RepositoryProperty<T, Param> = repositoryProperty(
    remoteSource = remoteSource,
    cachedSource = InMemoryCachedSource()
)

fun <T, Param : Parameters> repositoryProperty(
    remoteSource: RemoteSource<T, Param>,
    cachedSource: CachedSource<T, Param>
): RepositoryProperty<T, Param> = RepositoryPropertyImpl(
    remoteSource = remoteSource,
    cachedSource = cachedSource,
)

suspend fun <T> RepositoryProperty<T, Parameters.NONE>.load(forceUpdate: Boolean) =
    load(forceUpdate, Parameters.NONE)

interface RepositoryProperty<T, Param> where Param : Parameters {
    suspend fun load(
        forceUpdate: Boolean,
        parameters: Param
    ): T
}

private class RepositoryPropertyImpl<T, Param>(
    private val remoteSource: RemoteSource<T, Param>,
    private val cachedSource: CachedSource<T, Param>
) : RepositoryProperty<T, Param> where Param : Parameters {

    override suspend fun load(
        forceUpdate: Boolean,
        parameters: Param
    ): T {
        if (forceUpdate) {
            return loadInternal(parameters)
        }
        val cached = cachedSource.get(parameters)
        return cached ?: loadInternal(parameters)
    }

    private suspend fun loadInternal(parameters: Param): T {
        val result = remoteSource.load(parameters)
        cachedSource.put(result, parameters)
        return result
    }
}