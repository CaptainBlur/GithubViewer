package com.foxstoncold.githubviewer.screens

data class UserItem(
    val username: String,
    val avatarUrl: String,
    val htmlUrl: String
)

data class RepoItem(
    val name: String,
    val owner: UserItem,
    val description: String?,
    val createdAt: String,
    val updatedAt: String,
    val stars: Int,
    val watchers: Int,
    val forks: Int,
    val htmlUrl: String
)