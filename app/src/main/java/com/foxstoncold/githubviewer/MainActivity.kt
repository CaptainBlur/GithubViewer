package com.foxstoncold.githubviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.foxstoncold.githubviewer.screens.navigation.ScreenNavigation
import com.foxstoncold.githubviewer.ui.theme.GitHubTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: ScreenViewModel by viewModel()

        setContent {
            GitHubTheme {
                val systemUiController = rememberSystemUiController()
                systemUiController.setStatusBarColor(MaterialTheme.colorScheme.primary)

                ScreenNavigation(viewModel)
            }
        }
    }
}