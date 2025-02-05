package com.foxstoncold.githubviewer.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.foxstoncold.githubviewer.R
import com.foxstoncold.githubviewer.ScreenViewModel
import com.foxstoncold.githubviewer.sl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(vm: ScreenViewModel) {
    val items: List<SearchItemModel> by vm.searchItems.collectAsState(emptyList())

    var isSearchActive by remember { mutableStateOf(false) }
    val backAction = {
        if (isSearchActive)
            isSearchActive = false
    }
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    BackHandler(enabled = isSearchActive, onBack = backAction)

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
                    IconButton(modifier = Modifier
                            .padding(horizontal = 0.dp),
                            onClick = backAction
                        ){
                            Icon(modifier = Modifier
                                .size(28.dp),
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = ""
                            )
                    }
                },
                title = {
                    val focusedColor = MaterialTheme.colorScheme.tertiary
                    val unFocusedColor = MaterialTheme.colorScheme.surface
                    var borderColor by remember { mutableStateOf(unFocusedColor) }

                    val focusRequester = FocusRequester.Default

                    Box(modifier = Modifier
                        .fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart)
                    {
                        AnimatedVisibility(
                            visible = !isSearchActive,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Text(
                                text = "Поиск",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                        }

                        AnimatedVisibility(
                            visible = isSearchActive,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            BasicTextField(
                                value = searchText,
                                onValueChange = {
                                    searchText = it
                                },
                                modifier = Modifier
                                    .padding(horizontal = 6.dp, vertical = 10.dp)
                                    .fillMaxWidth()
                                    .onFocusChanged {
                                        borderColor =
                                            if (it.isFocused) focusedColor else unFocusedColor
                                    },
                                textStyle = LocalTextStyle.current.copy(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                cursorBrush = SolidColor(MaterialTheme.colorScheme.tertiary),
                                decorationBox = { innerTextField ->
                                    Box(
                                        modifier = Modifier
                                            .border(1.dp, borderColor, RoundedCornerShape(4.dp))
                                            .background(
                                                MaterialTheme.colorScheme.surface,
                                                RoundedCornerShape(4.dp)
                                            )
                                            .padding(8.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                modifier = Modifier
                                                    .padding(end = 10.dp)
                                                    .size(24.dp),
                                                imageVector = Icons.Filled.Search,
                                                tint = MaterialTheme.colorScheme.secondary,
                                                contentDescription = ""
                                            )
                                            innerTextField()
                                        }

                                        if (searchText.text.isEmpty())
                                            Text(
                                                modifier = Modifier
                                                    .padding(start = 35.dp),
                                                text = "Поиск",
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Normal,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                color = MaterialTheme.colorScheme.secondary
                                            )

                                        IconButton(modifier = Modifier
                                            .align(Alignment.CenterEnd),
                                            onClick = {
                                            searchText = TextFieldValue()
                                        }) {
                                            Icon(
                                                modifier = Modifier
                                                    .offset(x = 4.dp)
                                                    .size(22.dp),
                                                imageVector = Icons.Filled.Clear,
                                                tint = MaterialTheme.colorScheme.secondary,
                                                contentDescription = ""
                                            )
                                        }
                                    }
                                }
                            )
                        }

                        AnimatedVisibility(modifier = Modifier
                            .align(Alignment.CenterEnd),
                            visible = !isSearchActive,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ){
                            IconButton(onClick = { isSearchActive = true }) {
                                Icon(modifier = Modifier
                                    .size(28.dp),
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = ""
                                )
                            }
                        }

                    }
                }
            )
        }
    ){

        LazyColumn(modifier = Modifier
            .padding(it)
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
        ){
            items(items.size) { index ->
                GitHubItemCard(item = items[index], vm)
            }
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
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        if (item.type==1)
            RepoItemContent(item, vm)
        else
            UserItemContent(item)
    }
}

@Composable
fun RepoItemContent(item: SearchItemModel, vm: ScreenViewModel) {
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

@Composable
fun UserItemContent(item: SearchItemModel){
    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable(remember { MutableInteractionSource() }, indication = LocalIndication.current){
            sl.w("user item tap")
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
                text = item.name,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 12.dp)
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
}