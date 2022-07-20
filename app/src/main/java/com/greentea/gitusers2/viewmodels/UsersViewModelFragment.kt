package com.greentea.gitusers2.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greentea.gitusers2.services.models.DetailUserItem
import com.greentea.gitusers2.services.models.ListUsersResponseItem
import com.greentea.gitusers2.services.models.SearchUsersResponse
import com.greentea.gitusers2.services.models.repositories.UsersRepository
import com.greentea.gitusers2.utils.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class UsersViewModelFragment(private val usersRepository: UsersRepository): ViewModel() {
    val listUsers: MutableLiveData<Resources<List<ListUsersResponseItem>>> = MutableLiveData()
    private var listUsersPage = 1

    val searchUser: MutableLiveData<Resources<SearchUsersResponse>> = MutableLiveData()
    private var searchUserPage = 1

    val detailUserItem: MutableLiveData<Resources<DetailUserItem>> = MutableLiveData()
    private var detailUserPage = 1

    val followersUser: MutableLiveData<Resources<List<ListUsersResponseItem>>> = MutableLiveData()
    private var followersUserPage = 1

    val followingUser: MutableLiveData<Resources<List<ListUsersResponseItem>>> = MutableLiveData()
    private var followingUserPage = 1

    init {
        getListUsers()
    }

    private fun getListUsers() = viewModelScope.launch {
        listUsers.postValue(Resources.Loading())
        val response = usersRepository.getListUsers(listUsersPage)
        listUsers.postValue(handleListUsersResponse(response))
    }

     fun getResultSearch(querySearch: String) = viewModelScope.launch {
        searchUser.postValue(Resources.Loading())
        val response = usersRepository.getResultSearch(querySearch, searchUserPage)
        searchUser.postValue(handleSearchNewsResponse(response))
    }

    fun getUserDetail(username: String) = viewModelScope.launch {
        detailUserItem.postValue(Resources.Loading())
        val response = usersRepository.getDetailUser(username, detailUserPage)
        detailUserItem.postValue(handleDetailUserResponse(response))
    }

    fun getFollowersUser(username: String) = viewModelScope.launch {
        followersUser.postValue(Resources.Loading())
        val response = usersRepository.getFollowersUser(username, followersUserPage)
        followersUser.postValue(handleFollowersUsersResponse(response))
    }

    fun getFollowingUser(username: String) = viewModelScope.launch {
        followingUser.postValue(Resources.Loading())
        val response = usersRepository.getFollowingUser(username, followingUserPage)
        followingUser.postValue(handleFollowingUsersResponse(response))
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

    private fun handleSearchNewsResponse(
        response: Response<SearchUsersResponse>
    ) : Resources<SearchUsersResponse> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resources.Success(resultResponse)
            }
        }
        return Resources.Error(response.message())
    }

    private fun handleDetailUserResponse(
        response: Response<DetailUserItem>
    ) : Resources<DetailUserItem> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resources.Success(resultResponse)
            }
        }
        return Resources.Error(response.message())
    }

    private fun handleFollowersUsersResponse(
        response: Response<List<ListUsersResponseItem>>
    ): Resources<List<ListUsersResponseItem>> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resources.Success(resultResponse)
            }
        }
        return Resources.Error(response.message())
    }

    private fun handleFollowingUsersResponse(
        response: Response<List<ListUsersResponseItem>>
    ): Resources<List<ListUsersResponseItem>> {
        if(response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resources.Success(resultResponse)
            }
        }
        return Resources.Error(response.message())
    }

    fun insertFavoriteUser(user: ListUsersResponseItem) = viewModelScope.launch {
        usersRepository.insert(user)
    }

    fun getFavoriteUser() = usersRepository.getFavoriteUser()

    fun deleteFavoriteUser(user: ListUsersResponseItem) = viewModelScope.launch(Dispatchers.IO) {
        usersRepository.delete(user)
    }
}