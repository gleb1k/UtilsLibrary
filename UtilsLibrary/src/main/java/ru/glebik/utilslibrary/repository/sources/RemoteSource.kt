package ru.glebik.utilslibrary.repository.sources

import ru.glebik.utilslibrary.repository.Parameters

fun interface RemoteSource<T, Param> where Param : Parameters {
    suspend fun load(parameters: Param): T
}