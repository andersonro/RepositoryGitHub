package com.example.basicproject

import android.app.Application
import com.example.basicproject.data.di.DataModule
import com.example.basicproject.domain.di.DomainModule
import com.example.basicproject.presentation.di.PresentationModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
        }

        DataModule.load()
        DomainModule.load()
        PresentationModel.load()

    }
}