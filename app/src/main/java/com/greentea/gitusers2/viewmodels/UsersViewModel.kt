package com.greentea.gitusers2.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greentea.gitusers2.services.models.ListUsersResponseItem
import com.greentea.gitusers2.services.models.repositories.UsersRepository
import com.greentea.gitusers2.utils.Resources
import kotlinx.coroutines.launch
import retrofit2.Response

class UsersViewModel(private val usersRepository: UsersRepository): ViewModel() {
    private val listUsers: MutableLiveData<Resources<List<ListUsersResponseItem>>> = MutableLiveData()
    private var listUsersPage = 1

    init {
        getListUsers()
    }

    private fun getListUsers() = viewModelScope.launch {
        listUsers.postValue(Resources.Loading())
        val response = usersRepository.getListUsers(listUsersPage)
        listUsers.postValue(handleListUsersResponse(response))
    }

    private fun handleListUsersResponse(
        response: Response<List<ListUsersResponseItem>>
    ): Resources<List<ListUsersResponseItem>> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resources.Success(resultResponse)
            }
        }
        return Resources.Error(response.message())
    }
}