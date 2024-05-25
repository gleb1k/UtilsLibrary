package ru.glebik.utilslibrary.pref

sealed interface Preference<T> {
    fun get(): T
    fun put(value: T)
    fun hasValue(): Boolean
    fun clear()
}