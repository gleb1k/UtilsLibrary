package ru.glebik.utilslibrary.repository.sources

import ru.glebik.utilslibrary.repository.Parameters

interface CachedSource<T, Param> where Param : Parameters {
    suspend fun get(param: Param): T?
    suspend fun put(value: T, param: Param)
}