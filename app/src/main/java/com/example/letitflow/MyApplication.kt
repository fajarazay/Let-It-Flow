package com.example.letitflow

import android.app.Application
import androidx.multidex.MultiDex

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
    }
}
