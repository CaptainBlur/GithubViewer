package com.foxstoncold.githubviewer.screens.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.foxstoncold.githubviewer.ScreenViewModel
import com.foxstoncold.githubviewer.screens.FileExplorerScreen
import com.foxstoncold.githubviewer.screens.SearchScreen
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun ScreenNavigation(vm: ScreenViewModel) {
    val navController = rememberNavController()
    SideEffect {
        vm.passNavController(navController)
    }

    NavHost(navController = navController, startDestination = Screen.Search.route) {
        composable(Screen.Search.route) { SearchScreen(vm) }
        composable(Screen.FileExplorer.route) { backStackEntry ->
            //NavHost use this to present us a screen when we request navigation (like above)
            val repoName = backStackEntry.arguments?.getString("repoName") ?: return@composable
            FileExplorerScreen(repoName, vm)
        }
    }
}