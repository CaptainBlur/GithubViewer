package com.foxstoncold.githubviewer.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foxstoncold.githubviewer.R
import com.foxstoncold.githubviewer.ScreenViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun FileExplorerScreen(repoName: String, vm: ScreenViewModel){
    val currentPath by vm.currentPath.collectAsState()

    fun formatTitle(path: String): String {
        val length = 28

        return if (path.length > length) {
            "${path.take(5)}...${path.takeLast(length-7)}"
        } else {
            path
        }
    }

    BackHandler(onBack = vm::navigateBack)

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
                        onClick = vm::navigateBack
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
                        text = if (currentPath.isEmpty()) repoName else formatTitle("$repoName/$currentPath"),
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
                val fileType = item.type == "file"

                AnimatedContent(
                    targetState = item,
                    transitionSpec = { (slideInHorizontally(initialOffsetX = { it }) + fadeIn()).togetherWith(
                        fadeOut()
                    ) }
                ) { targetItem ->

                    if (!targetItem.stub) {
                        val uriHandler = LocalUriHandler.current

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (!fileType)
                                        vm.enterExplorerFolder(targetItem.path, targetItem.formatedContentsLink)
                                    else
                                        uriHandler.openUri(item.html_url)
                                }
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector =
                                if (fileType)
                                    ImageVector.vectorResource(R.drawable.outline_insert_drive_file_24)
                                else
                                    ImageVector.vectorResource(R.drawable.baseline_folder_24),
                                tint =
                                if (fileType)
                                    MaterialTheme.colorScheme.onBackground
                                else
                                    MaterialTheme.colorScheme.onTertiary,
                                contentDescription = null
                            )
                            Text(
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .weight(1f),
                                text = targetItem.name,
                                fontWeight = FontWeight.Medium
                            )
                            if (fileType) {
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = item.formatedSize,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                    }
                    else
                        ContentsItemStub()
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
fun ContentsItemStub(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        val height = 20.dp

        Box(modifier = Modifier
            .size(height)
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.surfaceContainer)
        )

        Box(modifier = Modifier
            .padding(start = 12.dp)
            .height(height)
            .width(156.dp)
            .clip(RoundedCornerShape(30))
            .background(MaterialTheme.colorScheme.surfaceContainer)
        )
    }
}