package ru.glebik.utilslibrary.keyboard

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText

fun EditText.closeOnDone() = setOnEditorActionListener { view, actionId, event ->
    val isHandled = (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED)
            && event?.action == KeyEvent.ACTION_DOWN
    if (isHandled) {
        KeyboardHelper.hideKeyboard(view)
        view.clearFocus()
    }
    isHandled
}
