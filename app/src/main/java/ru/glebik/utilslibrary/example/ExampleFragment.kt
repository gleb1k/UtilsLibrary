package ru.glebik.utilslibrary.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ru.glebik.utilslibrary.R
import ru.glebik.utilslibrary.adapter.CompositeAdapter
import ru.glebik.utilslibrary.adapter.castAdapterDelegate
import ru.glebik.utilslibrary.example.adapter.DateAdapter
import ru.glebik.utilslibrary.example.adapter.ExampleItem
import ru.glebik.utilslibrary.example.adapter.SomeAdapter
import ru.glebik.utilslibrary.keyboard.KeyboardHelper
import ru.glebik.utilslibrary.keyboard.KeyboardVisibilityListener
import ru.glebik.utilslibrary.keyboard.closeOnDone

class ExampleFragment : Fragment() {

    private val keyboardListener = object :
        ru.glebik.utilslibrary.keyboard.KeyboardVisibilityListener {
        override fun onKeyboardShown() {
            //Что-то сделать когда клавиатура в фокусе
            //Например как-то изменить ui
        }

        override fun onKeyboardHidden() {
            //Что-то сделать когда клавиатура уходит из фокуса
            //Например убрать фокус от EditText
        }
    }

    private lateinit var button: Button
    private lateinit var editText: EditText
    private lateinit var recycler: RecyclerView

    private val rvAdapter: ru.glebik.utilslibrary.adapter.CompositeAdapter<ExampleItem> by lazy {
        ru.glebik.utilslibrary.adapter.CompositeAdapter(
            DateAdapter().castAdapterDelegate(),
            SomeAdapter().castAdapterDelegate()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_example, container, false).apply {
            button = findViewById(R.id.btn_example)

            editText = findViewById<EditText?>(R.id.et_example).apply {
                //При нажатии Done будет убрана клавиатура, а также очищен фокус
                closeOnDone()
            }

            recycler = findViewById<RecyclerView?>(R.id.rv_example).apply {
                adapter = rvAdapter
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //При нажатии кнопки прятать клавиатуру
        button.setOnClickListener {
            ru.glebik.utilslibrary.keyboard.KeyboardHelper.hideKeyboard(view)
        }
    }

    override fun onResume() {
        super.onResume()
        //Регистрируем листенер
        ru.glebik.utilslibrary.keyboard.KeyboardHelper.registerListener(requireActivity(), keyboardListener)
    }

    override fun onStop() {
        //Чистим листенер
        ru.glebik.utilslibrary.keyboard.KeyboardHelper.unregisterListener(keyboardListener)
        super.onStop()
    }
}