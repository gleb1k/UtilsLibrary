package ru.glebik.utilslibrary.location


interface LocationHelper {
    fun hasPermissions(): Boolean
    suspend fun requireLocation(): Location
    suspend fun ensurePermissions()
}