package ru.glebik.utilslibrary.example.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.glebik.utilslibrary.R
import ru.glebik.utilslibrary.adapter.AdapterDelegate
import ru.glebik.utilslibrary.adapter.DelegateViewHolder
import ru.glebik.utilslibrary.adapter.ListItem

class DateAdapter : AdapterDelegate<DateItem> {
    override fun isForViewType(item: ListItem): Boolean = item is DateItem
    override fun createViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): DelegateViewHolder<DateItem> {
        return DateViewHolder(
            inflater.inflate(R.layout.item_date, parent, false),
        )
    }

    class DateViewHolder(
        view: View,
    ) : DelegateViewHolder<DateItem>(view) {

        override fun bind(item: DateItem) {
            with(item.date) {
                //биндим значения здесь
                //tv_date.text = date
            }
        }

    }
}