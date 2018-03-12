package ui.anwesome.com.kotlinllinetriview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.linetriview.LineTriView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LineTriView.create(this)
    }
}
