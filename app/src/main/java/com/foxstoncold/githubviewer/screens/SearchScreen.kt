package com.foxstoncold.githubviewer.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.foxstoncold.githubviewer.R
import com.foxstoncold.githubviewer.ScreenViewModel
import com.foxstoncold.githubviewer.sl

@Composable
fun SearchScreen(vm: ScreenViewModel) {
    val items: List<SearchItemModel> by vm.searchItems.collectAsState(emptyList())

    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.background)
    ){
        items(items.size) { index ->
            GitHubItemCard(item = items[index], vm)
        }
    }
}

@Composable
fun GitHubItemCard(item: SearchItemModel, vm: ScreenViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .animateContentSize()
            .clickable(remember { MutableInteractionSource() }, indication = LocalIndication.current){
                sl.w("card tap")
            },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Row {
                    item.starCount?.let { Text(text = " $it⭐ " , fontSize = 14.sp, color = MaterialTheme.colorScheme.primary) }
                    item.watcherCount?.let { Text(text = " $it👀 " , fontSize = 14.sp, color = MaterialTheme.colorScheme.primary) }
                    item.forkCount?.let { Text(text = " $it🍴 ", fontSize = 14.sp, color = MaterialTheme.colorScheme.primary) }
                }
            }
            Row(modifier = Modifier.padding(top = 8.dp)) {
                Text(
                    text = "Подробнее",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        vm.updateExpanded(item.id, !item.expanded)
                    }
                )
                Icon(modifier = Modifier
                    .padding(start = 2.dp)
                    .rotate(if (item.expanded) 90f else 0f),
                    imageVector = ImageVector.vectorResource(R.drawable.baseline_chevron_right_24),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
            if (item.expanded) {
                Box(modifier = Modifier
                    .padding(top = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxWidth()
                    .clickable(remember { MutableInteractionSource() }, indication = LocalIndication.current){
                        sl.w("user bar tap")
                    }
                ) {
                    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = rememberAsyncImagePainter(item.avatarUrl),
                            contentDescription = "",
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(50)),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = item.ownerName ?: "N/A",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Icon(modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 12.dp),
                        imageVector = ImageVector.vectorResource(R.drawable.baseline_chevron_right_24),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
                Text(
                    text = buildAnnotatedString {
                        append("Создан: ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.primary)) {
                            append(item.createdDate ?: "N/A")
                        }
                    },
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = buildAnnotatedString {
                        append("Обновлён: ")
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.primary)) {
                            append(item.updatedDate ?: "N/A")
                        }
                    },
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "Описание:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 8.dp)
                )
                item.description?.let {
                    Text(
                        text = it,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}