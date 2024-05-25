package ru.glebik.utilslibrary.pref

import android.content.Context
import android.content.SharedPreferences

object PrefManager {

    private const val DEFAULT_NAME = "prefs.manager.default"

    @Volatile
    private var preferencesInternal: SharedPreferences? = null

    fun init(context: Context) {
        preferencesInternal ?: synchronized(this) {
            preferencesInternal ?: context.getSharedPreferences(DEFAULT_NAME, Context.MODE_PRIVATE)
        }.also {
            preferencesInternal = it
        }
    }

    fun init(context: Context, name: String) {
        preferencesInternal ?: synchronized(this) {
            preferencesInternal ?: context.getSharedPreferences(name, Context.MODE_PRIVATE)
        }.also {
            preferencesInternal = it
        }
    }

    private fun getPrefInternal(): SharedPreferences {
        if (preferencesInternal != null)
            return preferencesInternal as SharedPreferences
        else throw NullPointerException("You must initialize PrefManager at first!")
    }


    fun intPreference(key: String): Preference<Int> = IntPreference(key, getPrefInternal())

    fun stringPreference(key: String): Preference<String> = StringPreference(key, getPrefInternal())

    fun booleanPreference(key: String): Preference<Boolean> =
        BooleanPreference(key, getPrefInternal())

    fun longPreference(key: String): Preference<Long> = LongPreference(key, getPrefInternal())

    fun floatPreference(key: String): Preference<Float> = FloatPreference(key, getPrefInternal())

}