package com.example.basicproject.repositories

import android.content.Context
import android.util.Log
import com.example.basicproject.R
import com.example.basicproject.core.RemoteException
import com.example.basicproject.data.service.GitHubService
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class RepoRepositoryImpl(private val service: GitHubService, private val context: Context) : RepoRepository {

    override suspend fun listRepositories(user: String) = flow {

        try {
            val repoList = service.ListRepositories(user)
            emit(repoList)
        }catch (ex: HttpException){

            if (ex.code()==404) {
                throw RemoteException(context.getString(R.string.msg_not_found))
            } else {
                throw RemoteException(ex.message?: "Não foi possível fazer a busca")
            }
        }

    }
}