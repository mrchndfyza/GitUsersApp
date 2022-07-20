package com.greentea.gitusers2.services.models

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UsersDao {
    //INSERT THE DATA TO THE DATABASE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: ListUsersResponseItem): Long

    //SHOW DATA FROM DATABASE
    @Query("SELECT * FROM dataForListUsers")
    fun getListUsers(): LiveData<List<ListUsersResponseItem>>

    //DELETE DATA FROM DATABASE
    @Delete
    fun delete(user: ListUsersResponseItem)
}