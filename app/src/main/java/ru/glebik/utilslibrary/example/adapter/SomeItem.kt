package ru.glebik.utilslibrary.example.adapter

import ru.glebik.utilslibrary.adapter.DiffListItem

class SomeItem(
    val someModel: SomeUiModel,
) : ExampleItem {

    override fun areItemsSame(other: DiffListItem): Boolean = other is SomeItem

    override fun areContentsSame(other: DiffListItem): Boolean = other is SomeItem
            && other.someModel == someModel

}

data class SomeUiModel(
    val id: Int,
    val name: Int,
)