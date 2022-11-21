package com.ivancaas.beersapp.presentation.ui.beer_detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.ivancaas.beersapp.R
import com.ivancaas.beersapp.data.remote.BeersResponse
import com.ivancaas.beersapp.presentation.ui.beer_detail.BeerDetailUiState.*
import com.ivancaas.beersapp.presentation.ui.beer_list.SplashScreen

@Composable
fun BeerDetailScreen(viewModel: BeerDetailViewModel, uiState: BeerDetailUiState) {


    when (uiState) {
        is Loading -> SplashScreen()
        is Success -> BeerDetailContent(uiState.beerDetail)
        is Failure -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "${uiState.error}")
        }
    }

}

@Composable
fun BeerDetailContent(beerDetail: BeersResponse.BeersResponseItem) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .memoryCache {
            MemoryCache.Builder(context)
                .maxSizePercent(0.25)
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(context.cacheDir.resolve("image_cache"))
                .maxSizePercent(0.02)
                .build()
        }
        .build()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = beerDetail.imageUrl,
            modifier = Modifier.weight(0.3f),
            contentDescription = beerDetail.name,
            imageLoader = imageLoader
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.7f)
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Text(
                text = beerDetail.name,
                color = Color.Black,
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.ExtraBold
            )
            Divider(modifier = Modifier.fillMaxWidth())
            DescriptionComponent(beerDetail.description)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.alcoholpv),
                    color = Color.Black,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "${beerDetail.abv}%",
                    color = Color.Black,
                    style = MaterialTheme.typography.subtitle1
                )

            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Elaborada por primera vez en: ",
                    color = Color.Black,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = beerDetail.firstBrewed,
                    color = Color.Black,
                    style = MaterialTheme.typography.subtitle1
                )
            }
        }
    }
}

@Composable
fun DescriptionComponent(description: String) {
    Text(buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = MaterialTheme.typography.h6.fontSize
            )
        ) {
            append("Descripción: ")
        }
        withStyle(
            style = SpanStyle(
                color = Color.Black,
                fontSize = MaterialTheme.typography.subtitle1.fontSize
            )
        ) {
            append(description)
        }
    }, color = Color.Black)
}
