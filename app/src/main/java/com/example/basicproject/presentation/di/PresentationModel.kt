package com.example.basicproject.presentation.di

import androidx.lifecycle.ViewModel
import com.example.basicproject.presentation.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

object PresentationModel {

    fun load() {
        loadKoinModules(viewModelModule())
    }

    private fun viewModelModule() : Module {
        return module {
            viewModel {
                MainViewModel(get())
            }
        }
    }
}