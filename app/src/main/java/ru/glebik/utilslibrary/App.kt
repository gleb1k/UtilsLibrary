package ru.glebik.utilslibrary

import android.app.Application
import android.util.Log
import ru.glebik.utilslibrary.auth.AuthTokenManagerImpl
import ru.glebik.utilslibrary.pref.PrefManager

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        ru.glebik.utilslibrary.pref.PrefManager.init(this) // инициализировали

        val intExamplePref = ru.glebik.utilslibrary.pref.PrefManager.intPreference("example")
        intExamplePref.put(100)
        val prefValue = intExamplePref.get()

        Log.d("PrefValue", prefValue.toString())

        val authTokenManager = ru.glebik.utilslibrary.auth.AuthTokenManagerImpl()
        authTokenManager.setAccess("access_token")
    }
}