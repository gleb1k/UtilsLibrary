package ru.glebik.utilslibrary

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ru.glebik.utilslibrary.keyboard.ActivityRootProvider

class MainActivity : AppCompatActivity(), ActivityRootProvider {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setContentView(R.layout.activity_main)
    }

    override fun root(): View = findViewById(R.id.container)
}
