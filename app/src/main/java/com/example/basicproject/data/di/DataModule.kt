package com.example.basicproject.data.di

import android.util.Log
import com.example.basicproject.data.service.GitHubService
import com.example.basicproject.repositories.RepoRepository
import com.example.basicproject.repositories.RepoRepositoryImpl
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataModule {

    private val URL_BASE = "https://api.github.com/"
    private val OK_HTTP = "OkHttp"

    fun load() {
        loadKoinModules(netWorkModule() + repositoriesModule())
    }

    private fun netWorkModule(): Module {
        return module {
            single {
                val interceptor = HttpLoggingInterceptor{
                    //Log.e(OK_HTTP, "networkModules $it")
                }

                interceptor.level = HttpLoggingInterceptor.Level.BODY

                OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build()
            }

            single {
                GsonConverterFactory.create(GsonBuilder().create())
            }

            single {
                createService<GitHubService>(get(), get())
            }
        }
    }

    private fun repositoriesModule(): Module {
        return module {
            single<RepoRepository> {
                RepoRepositoryImpl(get(), get())
            }
        }
    }

    private inline fun <reified T> createService(client: OkHttpClient, factory: GsonConverterFactory
    ) : T {
        return Retrofit.Builder()
            .baseUrl(URL_BASE)
            .client(client)
            .addConverterFactory(factory)
            .build()
            .create(T::class.java)
    }
}