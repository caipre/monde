package co.nickp.monde

import android.app.Application
import androidx.appcompat.app.AppCompatActivity

class App : Application()

internal val AppCompatActivity.app: App
    get() = (application as App)
