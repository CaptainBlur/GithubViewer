package com.foxstoncold.githubviewer.screens.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.foxstoncold.githubviewer.ScreenViewModel
import com.foxstoncold.githubviewer.screens.SearchScreen

@Composable
fun ScreenNavigation(vm: ScreenViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Search.route) {
        composable(Screen.Search.route) {
            SearchScreen(vm) { repoName ->
                navController.navigate(Screen.FileExplorer.createRoute(repoName))
            }
        }
        composable(Screen.FileExplorer.route) { backStackEntry ->
            val repoName = backStackEntry.arguments?.getString("repoName") ?: return@composable
            val path = backStackEntry.arguments?.getString("path") ?: ""

            Text("$repoName\t$path")
//            FileExplorerScreen(repoName, path, navController)
        }
    }
}