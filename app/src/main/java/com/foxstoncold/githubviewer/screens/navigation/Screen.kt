package com.foxstoncold.githubviewer.screens.navigation

sealed class Screen(val route: String) {
    data object Search : Screen("search")
    data object FileExplorer : Screen("file_explorer/{repoName}/{path}") {
        fun createRoute(repoName: String, path: String = "") = "file_explorer/$repoName/$path"
    }
}