package ru.glebik.utilslibrary.repository.sources

import ru.glebik.utilslibrary.repository.Parameters

class NoParamRemoteSource<T>(
    private val delegate: suspend () -> T
) : RemoteSource<T, Parameters.NONE> {
    override suspend fun load(parameters: Parameters.NONE): T = delegate()
}