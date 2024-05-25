package ru.glebik.utilslibrary.example.adapter

import ru.glebik.utilslibrary.adapter.DiffListItem

class DateItem(
    val date: DateUiModel,
) : ExampleItem {

    override fun areItemsSame(other: DiffListItem): Boolean = other is DateItem

    override fun areContentsSame(other: DiffListItem): Boolean = other is DateItem
            && other.date == date

}

data class DateUiModel(
    val date: Int,
    val month: String,
)