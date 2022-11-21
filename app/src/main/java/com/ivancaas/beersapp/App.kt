package com.ivancaas.beersapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.paperdb.Paper

@HiltAndroidApp
class App : Application() {
    //    private lateinit var firebaseAnalytics: FirebaseAnalytics
    companion object {
        lateinit var instance: App private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Paper.init(this)
//        firebaseAnalytics = Firebase.analytics
    }
}