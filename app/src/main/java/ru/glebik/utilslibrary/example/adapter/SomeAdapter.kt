package ru.glebik.utilslibrary.example.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.glebik.utilslibrary.R
import ru.glebik.utilslibrary.adapter.AdapterDelegate
import ru.glebik.utilslibrary.adapter.DelegateViewHolder
import ru.glebik.utilslibrary.adapter.ListItem


class SomeAdapter : ru.glebik.utilslibrary.adapter.AdapterDelegate<SomeItem> {
    override fun isForViewType(item: ru.glebik.utilslibrary.adapter.ListItem): Boolean = item is SomeItem
    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): ru.glebik.utilslibrary.adapter.DelegateViewHolder<SomeItem> {
        return DateViewHolder(
            inflater.inflate(R.layout.item_some, parent, false),
        )
    }

    class DateViewHolder(
        view: View,
    ) : ru.glebik.utilslibrary.adapter.DelegateViewHolder<SomeItem>(view) {

        override fun bind(item: SomeItem) {
            with(item.someModel) {
                //биндим
            }
        }

    }
}