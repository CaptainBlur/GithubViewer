package com.foxstoncold.githubviewer.screens

import com.foxstoncold.githubviewer.data.RepoData
import com.foxstoncold.githubviewer.data.UserData

data class SearchItemModel(
    val type: Int,
    val name: String,
    val description: String?,
    val avatarUrl: String,
    val profileUrl: String,
    val createdDate: String?,
    val updatedDate: String?,
    val starCount: Int?,
    val watcherCount: Int?,
    val forkCount: Int?,

    var expanded: Boolean = false
) {
    companion object {
        private fun from(user: UserData): SearchItemModel {
            return SearchItemModel(
                type = 0,
                name = user.login,
                description = null,
                avatarUrl = user.avatar_url,
                profileUrl = user.html_url,
                createdDate = null,
                updatedDate = null,
                starCount = null,
                watcherCount = null,
                forkCount = null
            )
        }

        private fun from(repo: RepoData): SearchItemModel {
            return SearchItemModel(
                type = 1,
                name = repo.name,
                description = repo.description,
                avatarUrl = repo.owner.avatar_url,
                profileUrl = repo.html_url,
                createdDate = repo.created_at,
                updatedDate = repo.updated_at,
                starCount = repo.stargazers_count,
                watcherCount = repo.watchers_count,
                forkCount = repo.forks_count
            )
        }

        fun mapUserList(list: List<UserData>): List<SearchItemModel> = list.map { from(it) }
        fun mapRepoList(list: List<RepoData>): List<SearchItemModel> = list.map { from(it) }
    }
}
