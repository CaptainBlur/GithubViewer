package com.foxstoncold.githubviewer.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.request.ImageRequest

@Composable
fun RepoItemContent(item: SearchItemModel, vm: ScreenViewModel) {
    val uriHandler = LocalUriHandler.current

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
                item.starCount?.let {
                    Text(
                        text = " $it⭐ ",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                item.watcherCount?.let {
                    Text(
                        text = " $it👀 ",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                item.forkCount?.let {
                    Text(
                        text = " $it🍴 ",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
        Row(modifier = Modifier.padding(top = 8.dp)) {
            Text(
                text = "Подробнее",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.clickable {
                    vm.updateExpanded(item.id, !item.expanded)
                }
            )
            Icon(
                modifier = Modifier
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
                .clickable(
                    remember { MutableInteractionSource() },
                    indication = LocalIndication.current
                ) {
                    uriHandler.openUri(item.profileUrl)
                }
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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

                Icon(
                    modifier = Modifier
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
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    ) {
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
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    ) {
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
    val uriHandler = LocalUriHandler.current

    Box(modifier = Modifier
        .fillMaxWidth()
        .clickable(
            remember { MutableInteractionSource() },
            indication = LocalIndication.current
        ) {
            uriHandler.openUri(item.profileUrl)
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

        Icon(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 12.dp),
            imageVector = ImageVector.vectorResource(R.drawable.baseline_chevron_right_24),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun SearchItemStub(){
    Box(modifier = Modifier
        .height(92.dp)
        .padding(top = 8.dp)
        .padding(horizontal = 12.dp)
        .clip(RoundedCornerShape(4.dp))
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.surfaceContainer)
    )
}

@Composable
fun EmptyQueryPlaceholder(modifier: Modifier){
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current
        val imageLoader = ImageLoader.Builder(context)
            .components {
                add(GifDecoder.Factory())
            }
            .build()
        Image(
            modifier = modifier.size(130.dp),
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(context).data(data = R.drawable.clipboard_gif).build(),
                imageLoader = imageLoader
            ),
            contentDescription = null,
        )
        BasicText(
            modifier = Modifier.padding(top = 16.dp),
            text = "Поиск",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            ),
        )
        BasicText(
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(horizontal = 16.dp),
            text = "Начните вводить поисковый запрос (от 3-х символов), чтобы увидеть его результаты 😊",
            style = MaterialTheme.typography.bodyMedium.copy(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.secondary
            ),
        )
    }

}

@Composable
fun EmptyListPlaceholder(modifier: Modifier){
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current
        val imageLoader = ImageLoader.Builder(context)
            .components {
                add(GifDecoder.Factory())
            }
            .build()
        Image(
            modifier = modifier.size(130.dp),
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(context).data(data = R.drawable.ufo_gif).build(),
                imageLoader = imageLoader
            ),
            contentDescription = null,
        )
        BasicText(
            modifier = Modifier.padding(top = 16.dp),
            text = "Тут ничего нет",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            ),
        )
        BasicText(
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(horizontal = 16.dp),
            text = "Возможно чуть позже здесь что-то появится",
            style = MaterialTheme.typography.bodyMedium.copy(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.secondary
            ),
        )
    }

}

@Composable
fun ErrorPlaceholder(modifier: Modifier, vm: ScreenViewModel){
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current
        val imageLoader = ImageLoader.Builder(context)
            .components {
                add(GifDecoder.Factory())
            }
            .build()
        Image(
            modifier = modifier.size(130.dp),
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(context).data(data = R.drawable.error_gif).build(),
                imageLoader = imageLoader
            ),
            contentDescription = null,
        )
        BasicText(
            modifier = Modifier.padding(top = 16.dp),
            text = "Упс...",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            ),
        )
        BasicText(
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(horizontal = 16.dp),
            text = "Либо у вас слабое интернет-подключение, либо что-то сломалось у нас - тогда мы в курсе и уже работаем над этим\n" +
                    "(вполне возможно, что вы превысили лимит на запросы - попробуйте позже или введите другой запрос)",
            style = MaterialTheme.typography.bodyMedium.copy(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.secondary
            ),
        )
        Button(modifier = Modifier
            .padding(top = 22.dp),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onSecondary,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            onClick = {
                vm.repeatSearchRequest()
            }) {
            BasicText(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = "Обновить",
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }

}