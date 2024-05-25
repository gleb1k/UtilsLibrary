package ru.glebik.utilslibrary.adapter

interface DiffListItem : ListItem {
    fun areItemsSame(other: DiffListItem): Boolean
    fun areContentsSame(other: DiffListItem): Boolean
}