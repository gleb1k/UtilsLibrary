package ru.glebik.utilslibrary.example.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.glebik.utilslibrary.R
import ru.glebik.utilslibrary.adapter.AdapterDelegate
import ru.glebik.utilslibrary.adapter.DelegateViewHolder
import ru.glebik.utilslibrary.adapter.ListItem


class SomeAdapter : AdapterDelegate<SomeItem> {
    override fun isForViewType(item: ListItem): Boolean = item is SomeItem
    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): DelegateViewHolder<SomeItem> {
        return DateViewHolder(
            inflater.inflate(R.layout.item_some, parent, false),
        )
    }

    class DateViewHolder(
        view: View,
    ) : DelegateViewHolder<SomeItem>(view) {

        override fun bind(item: SomeItem) {
            with(item.someModel) {
                //биндим
            }
        }

    }
}