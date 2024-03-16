package ru.glebik.utilslibrary.repository.sources

import ru.glebik.utilslibrary.repository.Parameters
import java.util.concurrent.ConcurrentHashMap

class InMemoryCachedSource<T, Params> : CachedSource<T, Params> where Params : Parameters {

    private val cache: ConcurrentHashMap<Params, T> = ConcurrentHashMap()

    override suspend fun get(param: Params): T? {
        return cache[param]
    }

    override suspend fun put(value: T, param: Params) {
        cache[param] = value
    }
}