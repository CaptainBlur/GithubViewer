package com.foxstoncold.githubviewer

import android.app.Application
import com.foxstoncold.githubviewer.di.appModule
import com.foxstoncold.splitlogger.SplitLogger
import org.koin.core.context.startKoin

typealias sl = SplitLogger

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()

        sl.initialize(this)
        sl.en()

        startKoin{
            modules(appModule)
        }
    }
}