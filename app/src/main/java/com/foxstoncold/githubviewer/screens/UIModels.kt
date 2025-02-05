package com.foxstoncold.githubviewer.screens

import com.foxstoncold.githubviewer.data.RepoData
import com.foxstoncold.githubviewer.data.UserData
import com.foxstoncold.githubviewer.sl
import java.text.SimpleDateFormat
import java.util.Locale

data class SearchItemModel(
    val type: Int,
    val id: Int,
    val stub: Boolean = false,

    val name: String,
    val ownerName: String? = null,
    val description: String? = null,
    val avatarUrl: String,
    val profileUrl: String,
    val repoUrl: String? = null,
    val createdDate: String? = null,
    val updatedDate: String? = null,
    val starCount: Int? = null,
    val watcherCount: Int? = null,
    val forkCount: Int? = null,

    val expanded: Boolean = false
) {
    companion object {
        private fun formatTimestamp(ts: String): String{
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                val date = inputFormat.parse(ts)
                date?.let { outputFormat.format(it) } ?: "N/A"
            } catch (e: Exception) {
                sl.s("error formating timestamp", e)
                "N/A"
            }
        }

        private fun from(user: UserData): SearchItemModel {
            return SearchItemModel(
                type = 0,
                id = user.id,
                name = user.login,
                avatarUrl = user.avatar_url,
                profileUrl = user.html_url,
            )
        }

        private fun from(repo: RepoData): SearchItemModel {
            return SearchItemModel(
                type = 1,
                id = repo.id,
                name = repo.name,
                ownerName = repo.owner.login,
                description = repo.description,
                avatarUrl = repo.owner.avatar_url,
                profileUrl = repo.owner.html_url,
                repoUrl = repo.url + "/contents",
                createdDate = formatTimestamp(repo.created_at),
                updatedDate = formatTimestamp(repo.updated_at),
                starCount = repo.stargazers_count,
                watcherCount = repo.watchers_count,
                forkCount = repo.forks_count
            )
        }

        fun mapUserList(list: List<UserData>): List<SearchItemModel> = list.map { from(it) }
        fun mapRepoList(list: List<RepoData>): List<SearchItemModel> = list.map { from(it) }
    }
}
