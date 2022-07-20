package com.greentea.gitusers2.services.models

data class SearchUsersResponse(
    val incomplete_results: Boolean,
    val items: List<ListUsersResponseItem>,
    val total_count: Int
)