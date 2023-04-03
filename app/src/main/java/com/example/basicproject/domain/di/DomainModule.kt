package com.example.basicproject.domain.di

import com.example.basicproject.domain.ListUserRepositoryUseCase
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

object DomainModule {

    fun load() {
        loadKoinModules(useCaseModule())
    }

    private fun useCaseModule(): Module {
        return module {
            factory {
                ListUserRepositoryUseCase(get())
            }
        }
    }
}