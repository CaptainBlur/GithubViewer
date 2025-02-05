package com.foxstoncold.githubviewer.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.foxstoncold.githubviewer.ScreenViewModel
import com.foxstoncold.githubviewer.data.TransmitStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(vm: ScreenViewModel, onRepoClick: (String) -> Unit) {
    val transmitStatus by vm.transmitStatus.collectAsState()
    val items: List<SearchItemModel> by vm.searchItems.collectAsState(emptyList())

    val searchText by vm.searchQuery.collectAsState()
    var isSearchActive by remember { mutableStateOf(searchText.text.isNotEmpty()) }
    val backAction = {
        if (isSearchActive)
            isSearchActive = false
    }

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
                                    vm.makeSearchRequest(it)
                                },
                                modifier = Modifier
                                    .padding(horizontal = 6.dp, vertical = 10.dp)
                                    .fillMaxWidth()
                                    .onFocusChanged {
                                        borderColor =
                                            if (it.isFocused) focusedColor else unFocusedColor
                                    },
                                singleLine = true,
                                textStyle = LocalTextStyle.current.copy(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                cursorBrush = SolidColor(MaterialTheme.colorScheme.tertiary),
                                decorationBox = { innerTextField ->
                                    Box(
                                        modifier = Modifier
                                            .border(1.dp, borderColor, RoundedCornerShape(4.dp))
                                            .background(
                                                MaterialTheme.colorScheme.onSurface,
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
                                                vm.clearList()
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
        val paddingMod = Modifier.padding(it)

        if (searchText.text.length<3)
            EmptyQueryPlaceholder(paddingMod)
        else if (transmitStatus==TransmitStatus.READY && items.isEmpty())
            EmptyListPlaceholder(paddingMod)
        else if (transmitStatus==TransmitStatus.READY || transmitStatus==TransmitStatus.LOADING)
            LazyColumn(modifier = paddingMod
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
            ){
                items(items.size) { index ->
                    val item = items[index]

                    if (index==0)
                        Spacer(modifier = Modifier
                            .height(8.dp))
                    if (!item.stub)
                        GitHubItemCard(item = item, vm, onRepoClick)
                    else
                        ItemStub()
                }
            }
        else
            ErrorPlaceholder(modifier = Modifier.padding(it), vm)

    }

}

@Composable
fun GitHubItemCard(item: SearchItemModel, vm: ScreenViewModel, onRepoClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .animateContentSize()
            .clickable(
                enabled = !item.stub && item.type==1,
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current)
            {
                onRepoClick.invoke(item.name)
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