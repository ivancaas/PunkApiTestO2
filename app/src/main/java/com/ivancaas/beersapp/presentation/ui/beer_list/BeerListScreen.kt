package com.ivancaas.beersapp.presentation.ui.beer_list

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.ivancaas.beersapp.R
import com.ivancaas.beersapp.data.remote.BeersResponse
import com.ivancaas.beersapp.presentation.ui.beer_list.BeerListUiState.*
import com.ivancaas.beersapp.util.then
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BeerListScreen(viewModel: BeerListViewModel, uiState: BeerListUiState) {

    when (uiState) {
        is Loading -> SplashScreen()
        is Success -> BeerListContent(
            uiState.beerList,
            viewModel::getBeerList,
            viewModel::navigateToDetail,
            viewModel.noMorePages,
            viewModel.beerLoading,
            viewModel.currentPage
        )
        is Failure -> println(uiState.error)
    }

}

@Composable
fun SplashScreen() {

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        val imageLoader = ImageLoader.Builder(LocalContext.current)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
        AsyncImage(
            model = R.drawable.cheer_beer,
            contentDescription = "LOGO",
            imageLoader = imageLoader
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BeerListContent(
    beerList: ArrayList<BeersResponse.BeersResponseItem>,
    getBeerList: suspend (String, Boolean) -> Unit,
    navigateToDetail: (String) -> Unit,
    noMorePages: MutableState<Boolean>,
    beerLoading: MutableState<Boolean>,
    currentPage: MutableState<Int>
) {
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

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        val listState = rememberLazyListState()
        val staggeredGridState = rememberLazyStaggeredGridState()
        val gridState = rememberLazyGridState()
        val beerName = rememberSaveable {
            mutableStateOf("")
        }
        val isList = rememberSaveable {
            mutableStateOf(true)
        }
        val staggered =
            rememberSaveable { //Without saveable it won't come back from detail as list or grid(depending on what selected the user)
                mutableStateOf(false)
            }
        val coroutineScope = rememberCoroutineScope()
        TextField(
            value = beerName.value,
            modifier = Modifier.padding(vertical = 10.dp),
            onValueChange = {
                beerName.value = it
                currentPage.value = 1
                noMorePages.value = false
                coroutineScope.launch {

                    getBeerList(beerName.value, false)
                    listState.scrollToItem(0)
                    gridState.scrollToItem(0)
                    staggeredGridState.scrollToItem(0)

                }
            },
            placeholder = { Text(text = "Nombre de la Cerveza") },
            trailingIcon = {
                if (beerName.value.isNotBlank()) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "sdsd",
                        modifier = Modifier
                            .offset(x = 10.dp)
                            .clickable {
                                beerName.value = ""
                            })
                }
            }
        )

        GridListSelector(isList, staggered)

        if (isList.value) {
            LazyColumn(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .weight(1f), state = listState
            ) {
                itemsIndexed(beerList) { index, item ->
                    if (index == (beerList.size - 5)) { // To get + beers transparently for the user
                        LaunchedEffect(key1 = null) {
                            if (!noMorePages.value)
                                getBeerList(beerName.value, true)
                        }
                    }
                    BeerListItem(item, imageLoader, navigateToDetail)
                }
                if (beerLoading.value)
                    item {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            CircularProgressIndicator(color = Color.Red)
                        }
                    }

            }
        } else {
            val loading = remember { mutableStateOf(true) }
            if (staggered.value) {
                LaunchedEffect(key1 = null) {
                    delay(1500)
                    //TO PREVENT BUGGY UI (DIFFERENT SIZE OF IMAGENES WITHOUT LOADING)
                    loading.value = false
                }
                Box(modifier = Modifier.weight(1f)) {

                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(4),
                        modifier = Modifier.fillMaxSize(1f),
                        state = staggeredGridState
                    ) {

                        itemsIndexed(beerList) { index, item ->
                            if (index == (beerList.size - 5)) { // To get + beers transparently for the user
                                LaunchedEffect(key1 = null) {
                                    if (!noMorePages.value)
                                        getBeerList(beerName.value, true)
                                }
                            }
                            BeerStaggeredItem(
                                beerItem = item,
                                imageLoader = imageLoader,
                                navigateToDetail
                            )

                        }
                    }
                    if (loading.value) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White), contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color.Red)
                        }
                    }
                }

            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.weight(1f),
                    state = gridState
                ) {
                    itemsIndexed(beerList) { index, item ->
                        if (index == (beerList.size - 10)) { // To get + beers transparently for the user
                            LaunchedEffect(key1 = null) {
                                if (!noMorePages.value)
                                    getBeerList(beerName.value, true)
                            }
                        }

                        BeerGridItem(beerItem = item, imageLoader = imageLoader, navigateToDetail)
                    }
                }
            }
        }
    }
}

@Composable
fun BeerListItem(
    beerItem: BeersResponse.BeersResponseItem,
    imageLoader: ImageLoader,
    navigateToDetail: (String) -> Unit
) {

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 5.dp)
        .height(100.dp)
        .clip(RoundedCornerShape(5.dp))
        .shadow(5.dp)
        .padding(10.dp)
        .clickable {
            navigateToDetail(beerItem.id.toString())
        }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = beerItem.imageUrl,
                modifier = Modifier.weight(0.2f),
                contentDescription = beerItem.name,
                imageLoader = imageLoader
            )
            Column(modifier = Modifier.weight(0.8f), verticalArrangement = Arrangement.Center) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) { // No need of horizontalArrangement because some titles are so long and I put weight
                    Text(
                        text = beerItem.name,
                        color = Color.Black,
                        modifier = Modifier.weight(0.7f),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Alc. ${beerItem.abv}%",
                        color = Color.Black,
                        modifier = Modifier.weight(0.3f),
                        fontWeight = FontWeight.Bold
                    )

                }
                Text(
                    text = beerItem.description,
                    color = Color.Black,
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }
    }
}

@Composable
fun BeerGridItem(
    beerItem: BeersResponse.BeersResponseItem,
    imageLoader: ImageLoader,
    navigateToDetail: (String) -> Unit
) {
    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier
        .width(IntrinsicSize.Max)
        .clickable {
            navigateToDetail(beerItem.id.toString())

        }) {
        AsyncImage(
            model = beerItem.imageUrl,
            modifier = Modifier.height(200.dp),
            contentDescription = beerItem.name,
            imageLoader = imageLoader
        )
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(vertical = 10.dp)
        ) {
            Text(
                text = beerItem.name,
                maxLines = 2,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun BeerStaggeredItem(
    beerItem: BeersResponse.BeersResponseItem,
    imageLoader: ImageLoader,
    navigateToDetail: (String) -> Unit
) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.clickable {
        navigateToDetail(beerItem.id.toString())
    }) {
        AsyncImage(
            model = beerItem.imageUrl,
            placeholder = painterResource(id = R.drawable.placeholder),
            modifier = Modifier.border(width = 4.dp, color = Color.Black),
            contentDescription = beerItem.name,
            imageLoader = imageLoader
        )
    }
}

@Composable
fun GridListSelector(listSelected: MutableState<Boolean>, staggeredGrid: MutableState<Boolean>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, end = 10.dp), contentAlignment = Alignment.CenterEnd
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!listSelected.value) {
                Text(text = "Staggered Grid ", color = Color.Black)
                Switch(
                    checked = staggeredGrid.value,
                    onCheckedChange = { staggeredGrid.value = !staggeredGrid.value })
            }
            Row(
                Modifier
                    .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(5.dp))
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.list), modifier = Modifier
                        .alpha(listSelected.value.then(1f) ?: 0.5f)
                        .padding(5.dp)
                        .size(32.dp)
                        .clickable {
                            listSelected.value = true
                        }, contentDescription = "LIST"
                )
                Divider(
                    Modifier
                        .fillMaxHeight()
                        .width(2.dp), color = Color.Black
                )
                Image(
                    painter = painterResource(id = R.drawable.grid), modifier = Modifier
                        .alpha(listSelected.value.then(0.5f) ?: 1f)
                        .padding(5.dp)
                        .size(32.dp)
                        .clickable {
                            listSelected.value = false
                        }, contentDescription = "GRID"
                )
            }
        }

    }
}
