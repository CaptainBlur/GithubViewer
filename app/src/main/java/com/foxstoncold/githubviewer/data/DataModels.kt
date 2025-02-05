package com.foxstoncold.githubviewer.data


data class SearchResponseWrapper<T>(
    val items: List<T>
)

data class UserData(
    val id: Int,
    val login: String,
    val avatar_url: String,
    val html_url: String,
)

data class RepoData(
    val id: Int,
    val name: String,
    val owner: UserData,
    val description: String?,
    val created_at: String,
    val updated_at: String,
    val stargazers_count: Int,
    val watchers_count: Int,
    val forks_count: Int,
    val url: String
)

data class RepoContentItem(
    val name: String,
    val path: String,
    val type: String,
    val url: String,
    val html_url: String
)