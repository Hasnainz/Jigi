package com.example.jigi

import android.app.Application
import com.example.jigi.data.AppContainer
import com.example.jigi.data.AppDataContainer

class DictionaryApplication : Application() {
    lateinit var container : AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}