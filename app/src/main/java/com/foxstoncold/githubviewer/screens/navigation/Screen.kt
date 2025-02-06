package com.foxstoncold.githubviewer.screens.navigation

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class Screen(val route: String) {
    data object Search : Screen("search")
    data object FileExplorer : Screen("file_explorer/{repoName}") {
        fun createRoute(repoName: String) = "file_explorer/$repoName"
    }
}