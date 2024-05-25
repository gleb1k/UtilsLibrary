package ru.glebik.utilslibrary.pref

import android.content.SharedPreferences
import androidx.core.content.edit

class IntPreference(
    private val key: String,
    private val sharedPreferences: SharedPreferences,
) : Preference<Int> {
    override fun get(): Int = sharedPreferences.getInt(key, -1)

    override fun hasValue(): Boolean = sharedPreferences.contains(key)

    override fun put(value: Int) = sharedPreferences.edit {
        putInt(key, value)
    }

    override fun clear() = sharedPreferences.edit { remove(key) }
}

class StringPreference(
    private val key: String,
    private val sharedPreferences: SharedPreferences,
) : Preference<String> {
    override fun get(): String = sharedPreferences.getString(key, "") as String

    override fun hasValue(): Boolean = sharedPreferences.contains(key)

    override fun put(value: String) = sharedPreferences.edit {
        putString(key, value)
    }

    override fun clear() = sharedPreferences.edit { remove(key) }
}

class BooleanPreference(
    private val key: String,
    private val sharedPreferences: SharedPreferences,
) : Preference<Boolean> {
    override fun get(): Boolean = sharedPreferences.getBoolean(key, false)

    override fun hasValue(): Boolean = sharedPreferences.contains(key)

    override fun put(value: Boolean) = sharedPreferences.edit {
        putBoolean(key, value)
    }

    override fun clear() = sharedPreferences.edit { remove(key) }
}

class LongPreference(
    private val key: String,
    private val sharedPreferences: SharedPreferences,
) : Preference<Long> {
    override fun get(): Long = sharedPreferences.getLong(key, -1L)

    override fun hasValue(): Boolean = sharedPreferences.contains(key)

    override fun put(value: Long) = sharedPreferences.edit {
        putLong(key, value)
    }

    override fun clear() = sharedPreferences.edit { remove(key) }
}

class FloatPreference(
    private val key: String,
    private val sharedPreferences: SharedPreferences,
) : Preference<Float> {
    override fun get(): Float = sharedPreferences.getFloat(key, -1F)

    override fun hasValue(): Boolean = sharedPreferences.contains(key)

    override fun put(value: Float) = sharedPreferences.edit {
        putFloat(key, value)
    }

    override fun clear() = sharedPreferences.edit { remove(key) }
}