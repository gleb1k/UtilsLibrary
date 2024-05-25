package ru.glebik.utilslibrary.auth

import ru.glebik.utilslibrary.pref.PrefManager

interface AuthTokenManager {
    fun setAccess(access: String)
    fun access(): String
    fun setRefresh(refresh: String)
    fun refresh(): String

    fun clearAuthData()
}

internal class AuthTokenManagerImpl(
    accessPrefName: String = ACCESS_KEY,
    refreshPrefName: String = REFRESH_KEY,
) : AuthTokenManager {

    private val accessPreference = PrefManager.stringPreference(accessPrefName)
    private val refreshPreference = PrefManager.stringPreference(refreshPrefName)

    override fun access(): String = accessPreference.get()

    override fun refresh(): String = refreshPreference.get()

    override fun setAccess(access: String) = accessPreference.put(access)

    override fun setRefresh(refresh: String) = refreshPreference.put(refresh)

    override fun clearAuthData() {
        accessPreference.clear()
        refreshPreference.clear()
    }

    private companion object {
        const val ACCESS_KEY = "auth.access.default"
        const val REFRESH_KEY = "auth.refresh.default"
    }
}