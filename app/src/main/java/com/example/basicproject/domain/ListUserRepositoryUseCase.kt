package com.example.basicproject.domain

import com.example.basicproject.data.model.Repositories
import com.example.basicproject.repositories.RepoRepository
import kotlinx.coroutines.flow.Flow

class ListUserRepositoryUseCase(private val repository: RepoRepository) {

    suspend fun execute(param: String): Flow<List<Repositories>> {
        return repository.listRepositories(param)
    }
}