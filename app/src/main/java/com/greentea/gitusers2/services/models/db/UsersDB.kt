package com.greentea.gitusers2.services.models.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.greentea.gitusers2.services.models.ListUsersResponseItem
import com.greentea.gitusers2.services.models.UsersDao

@Database(
    entities = [ListUsersResponseItem::class],
    version = 1
)

abstract class UsersDB : RoomDatabase() {
    abstract fun getUserDao(): UsersDao

    companion object {
        @Volatile
        private var instance: UsersDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance?: createDatabase(context).also {
                instance = it
            }
        }

        //CREATE DATABASE
        private fun createDatabase(context: Context) =
            Room
                .databaseBuilder(
                    context.applicationContext, UsersDB::class.java, "user.db"
                )
                .build()
    }
}