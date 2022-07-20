package com.greentea.gitusers2.services.api

import com.greentea.gitusers2.BuildConfig
import com.greentea.gitusers2.services.models.DetailUserItem
import com.greentea.gitusers2.services.models.ListUsersResponseItem
import com.greentea.gitusers2.services.models.SearchUsersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

//GET THE API KEY
private const val mySuperSecretKey = BuildConfig.API_KEY

interface UsersAPI {
    @Headers("Authorization: token $mySuperSecretKey")

    //GET LIST USERS
    @GET("users")
    suspend fun getListUsers(
        @Query("page")
        pageNumber: Int = 1
    ): Response<List<ListUsersResponseItem>>

    //GET RESULT OF SEARCH USER
    @GET("search/users")
    suspend fun getSearchResults(
        @Query("q")
        query: String,
        @Query("page")
        pageNumber: Int = 1
    ): Response<SearchUsersResponse>

    //GET DETAIL USER
    @GET("users/{username}")
    suspend fun getDetailUser(
        @Path("username")
        username: String,
        @Query("page")
        pageNumber: Int = 1
    ): Response<DetailUserItem>

    //GET FOLLOWERS USER
    @GET("users/{username}/followers")
    suspend fun getFollowersUser(
        @Path("username")
        username: String,
        @Query("page")
        pageNumber: Int = 1
    ): Response<List<ListUsersResponseItem>>

    //GET FOLLOWING USER
    @GET("users/{username}/following")
    suspend fun getFollowingUser(
        @Path("username")
        username: String,
        @Query("page")
        pageNumber: Int = 1
    ): Response<List<ListUsersResponseItem>>
}