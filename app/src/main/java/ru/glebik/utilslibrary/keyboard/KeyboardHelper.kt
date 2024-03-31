package ru.glebik.utilslibrary.keyboard

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager

object KeyboardHelper {

    private class OnGlobalLayoutListenerImpl(
        val root: View,
        val keyboardListener: KeyboardVisibilityListener
    ) : ViewTreeObserver.OnGlobalLayoutListener {

        enum class State { SHOWN, HIDDEN }

        private var lastState: State? = null
        private val rect = Rect()

        init {
            root.viewTreeObserver.addOnGlobalLayoutListener(this)
        }

        override fun onGlobalLayout() {
            root.getWindowVisibleDisplayFrame(rect)
            val height = root.height
            val keypadHeight = height - rect.bottom
            val newState = if (keypadHeight > height * 0.15) {
                State.SHOWN
            } else {
                State.HIDDEN
            }
            if (newState == lastState) return
            lastState = newState
            when (lastState) {
                State.SHOWN -> keyboardListener.onKeyboardShown()
                State.HIDDEN -> keyboardListener.onKeyboardHidden()
                else -> Unit
            }
        }

        fun detach() {
            root.viewTreeObserver.removeOnGlobalLayoutListener(this)
        }
    }

    private val listeners = mutableListOf<OnGlobalLayoutListenerImpl>()

    fun registerListener(
        activity: Activity,
        listener: KeyboardVisibilityListener
    ) {
        if (activity !is ActivityRootProvider)
            throw IllegalArgumentException("Activity must implement ActivityRootProvider")
        val root = activity.root()
        listeners += OnGlobalLayoutListenerImpl(root, listener)
    }

    fun unregisterListener(listener: KeyboardVisibilityListener) {
        val internal = listeners.find { it.keyboardListener === listener }
        internal ?: return
        internal.detach()
        listeners.remove(internal)
    }

    fun hideKeyboard(view: View) {
        if (view.hasFocus().not())
            return
        val manager =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}