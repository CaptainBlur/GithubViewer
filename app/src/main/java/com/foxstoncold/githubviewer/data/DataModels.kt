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

data class ExplorerContentItem(
    val name: String = String(),
    val path: String = String(),
    val size: Int = -1,
    val type: String = String(),
    val url: String = String(),
    val html_url: String = String(),

    //Assigning these values in VM, because I don't want to create a separate UI-model to map this model
    var formatedSize: String = "",
    var formatedContentsLink: String = url,
    val stub: Boolean
)