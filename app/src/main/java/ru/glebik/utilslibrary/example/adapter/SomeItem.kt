package ru.glebik.utilslibrary.example.adapter

import ru.glebik.utilslibrary.adapter.DiffListItem

class SomeItem(
    val someModel: SomeUiModel,
) : ExampleItem {

    override fun areItemsSame(other: ru.glebik.utilslibrary.adapter.DiffListItem): Boolean = other is SomeItem

    override fun areContentsSame(other: ru.glebik.utilslibrary.adapter.DiffListItem): Boolean = other is SomeItem
            && other.someModel == someModel

}

data class SomeUiModel(
    val id: Int,
    val name: Int,
)