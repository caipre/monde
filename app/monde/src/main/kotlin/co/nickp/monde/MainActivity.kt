package co.nickp.monde

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import co.nickp.monde.android.theme.TypographyFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, TypographyFragment())
                .commitNow()
        }
    }
}
