package com.example.basicproject.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.basicproject.data.model.Repositories
import com.example.basicproject.domain.ListUserRepositoryUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MainViewModel(private val listUserRepositoriesUseCase: ListUserRepositoryUseCase) : ViewModel() {

    private val _repos = MutableLiveData<State>()
    val repos: LiveData<State> = _repos

    fun getRepositoriesList(user: String) {

        viewModelScope.launch {
            listUserRepositoriesUseCase.execute(user)
                .onStart {
                    _repos.postValue(State.Loading)
                }
                .catch {
                    _repos.postValue(State.Error(it))
                }
                .collect {
                    _repos.postValue(State.Success(it))
                }
        }
    }

    sealed class State{
        object Loading: State()
        data class Success(val list: List<Repositories>): State()
        data class Error(val error: Throwable) : State()
    }
}