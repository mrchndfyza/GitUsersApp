package com.greentea.gitusers2.services.models

data class DetailUserItem(
    val avatar_url: String,
    var name: String? = null,
    var login: String? = null,
    var followers: Int = 0,
    var following: Int = 0,
    var company: String? = null,
    var location: String? = null,
    var public_repos: Int? = 0,
)