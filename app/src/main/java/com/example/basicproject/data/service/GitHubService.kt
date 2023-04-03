package com.example.basicproject.data.service

import com.example.basicproject.data.model.Repositories
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubService {

    @GET("users/{user}/repos")
    suspend fun ListRepositories(@Path("user") user: String) : List<Repositories>
}