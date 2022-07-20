package com.greentea.gitusers2.services.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "dataForListUsers"
)
data class ListUsersResponseItem(
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    val avatar_url: String,
    val login: String,
    val html_url: String,
    val type: String,
) : Serializable