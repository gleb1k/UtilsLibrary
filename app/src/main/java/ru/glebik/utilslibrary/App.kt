package ru.glebik.utilslibrary

import android.app.Application
import android.util.Log
import ru.glebik.utilslibrary.auth.AuthTokenManagerImpl
import ru.glebik.utilslibrary.pref.PrefManager
import ru.glebik.utilslibrary.pref.PrefManager.intPreference

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        PrefManager.init(this) // инициализировали

        val intExamplePref = intPreference("example")
        intExamplePref.put(100)
        val prefValue = intExamplePref.get()

        Log.d("PrefValue", prefValue.toString())

        val authTokenManager = AuthTokenManagerImpl()
        authTokenManager.setAccess("access_token")
    }
}