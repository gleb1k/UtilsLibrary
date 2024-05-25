package ru.glebik.utilslibrary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup

interface AdapterDelegate<T : ListItem> {
    fun isForViewType(item: ListItem): Boolean
    fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): DelegateViewHolder<T>
}

@Suppress("UNCHECKED_CAST")
fun <T : DiffListItem> AdapterDelegate<*>.castAdapterDelegate(): AdapterDelegate<T> =
    this as AdapterDelegate<T>
