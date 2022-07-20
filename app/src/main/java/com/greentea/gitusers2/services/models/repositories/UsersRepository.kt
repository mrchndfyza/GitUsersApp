package com.greentea.gitusers2.services.models.repositories

import com.greentea.gitusers2.services.models.ListUsersResponseItem
import com.greentea.gitusers2.services.models.db.UsersDB
import com.greentea.gitusers2.services.retrofit.RetrofitInstance

class UsersRepository(val db: UsersDB) {
    suspend fun getListUsers(pageNumber: Int) = RetrofitInstance.apiObject.getListUsers(pageNumber)

    suspend fun getResultSearch(query: String, pageNumber: Int) =
        RetrofitInstance.apiObject.getSearchResults(query, pageNumber)

    suspend fun getDetailUser(username: String, pageNumber: Int) =
        RetrofitInstance.apiObject.getDetailUser(username, pageNumber)

    suspend fun getFollowersUser(username: String, pageNumber: Int) =
        RetrofitInstance.apiObject.getFollowersUser(username, pageNumber)

    suspend fun getFollowingUser(username: String, pageNumber: Int) =
        RetrofitInstance.apiObject.getFollowingUser(username, pageNumber)

    suspend fun insert(user: ListUsersResponseItem) = db.getUserDao().insert(user)

    fun getFavoriteUser() = db.getUserDao().getListUsers()

    fun delete(user: ListUsersResponseItem) = db.getUserDao().delete(user)
}