package com.foxstoncold.githubviewer.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foxstoncold.githubviewer.R
import com.foxstoncold.githubviewer.ScreenViewModel
import com.foxstoncold.githubviewer.data.ExplorerContentItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileExplorerScreen(path: String, vm: ScreenViewModel){
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(
                        modifier = Modifier
                            .padding(horizontal = 0.dp),
                        onClick = vm::popupFileExplorer
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(28.dp),
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                },
                title={
                    Text(
                        text = path,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    ){
        val contents by vm.explorerContents.collectAsState()

        LazyColumn(modifier = Modifier
            .padding(it)
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
            ) {
            items(contents.size) { index ->
                val item = contents[index]

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            vm.enterExplorerRepo(item.path, item.url)
//                            navController.navigate(Screen.FileExplorer.createRoute(repoName, "$currentPath/$item"))
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector =
                        if (item.type=="file")
                            ImageVector.vectorResource(R.drawable.outline_insert_drive_file_24)
                        else
                            ImageVector.vectorResource(R.drawable.baseline_folder_24),
                        tint =
                        if (item.type=="file")
                            MaterialTheme.colorScheme.onBackground
                        else
                            MaterialTheme.colorScheme.onTertiary,
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .weight(1f),
                        text = item.name,
                        fontWeight = FontWeight.Medium
                    )
                    if (item.type=="file") {
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = item.formatedSize,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.secondary)
            }
        }
    }
}