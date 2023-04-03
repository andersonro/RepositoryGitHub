package com.example.basicproject.repositories

import com.example.basicproject.data.model.Repositories
import kotlinx.coroutines.flow.Flow

interface RepoRepository {
    suspend fun listRepositories(user: String) : Flow<List<Repositories>>
}