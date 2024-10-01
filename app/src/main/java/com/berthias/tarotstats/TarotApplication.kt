package com.berthias.tarotstats

import android.app.Application
import com.berthias.tarotstats.data.AppContainer

class TarotApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        application = this
        appContainer = AppContainer(this)
    }

    companion object {
        lateinit var application: TarotApplication
    }

    lateinit var appContainer: AppContainer
}