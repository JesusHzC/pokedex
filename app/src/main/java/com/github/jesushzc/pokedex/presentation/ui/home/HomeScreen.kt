package com.github.jesushzc.pokedex.presentation.ui.home

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.jesushzc.pokedex.R
import com.github.jesushzc.pokedex.presentation.components.CardPokemon
import com.github.jesushzc.pokedex.presentation.components.CustomScaffold
import com.github.jesushzc.pokedex.presentation.components.ErrorScreen
import com.github.jesushzc.pokedex.presentation.components.GridShimmer
import com.github.jesushzc.pokedex.presentation.components.SearchTextField
import com.github.jesushzc.pokedex.presentation.navigation.Routes
import com.github.jesushzc.pokedex.utils.replaceWithSharp
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    animatedVisibilityScope: AnimatedVisibilityScope,
    onNavigateTo: (String) -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyGridState()

    var showFloatButton by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = lazyListState) {
        snapshotFlow {
            lazyListState.firstVisibleItemIndex
        }.collectLatest {
            showFloatButton = it > 10
        }
    }

    CustomScaffold(
        title = "Pokedex",
        showToolbar = false,
        showFloatingButton = showFloatButton,
        currentRoute = Routes.HOME_SCREEN,
        onNavigateTo = onNavigateTo,
        onFloatingButtonClicked = {
            coroutineScope.launch {
                lazyListState.scrollToItem(0)
            }
        }
    ) {
        HomeContent(
            lazyListState = lazyListState,
            viewModel = viewModel,
            animatedVisibilityScope = animatedVisibilityScope,
            onNavigateTo = onNavigateTo,
            onError = {
                showFloatButton = false
            }
        )
    }

}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun SharedTransitionScope.HomeContent(
    lazyListState: LazyGridState,
    viewModel: HomeViewModel,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onNavigateTo: (String) -> Unit,
    onError: () -> Unit
) {
    val state = viewModel.state
    val gridColumns = 2

    val maxHeight = 80f
    val minHeight = 0f
    val density = LocalDensity.current.density

    val toolbarHeightPx = with(LocalDensity.current) {
        maxHeight.dp.roundToPx().toFloat()
    }

    val toolbarMinHeightPx = with(LocalDensity.current) {
        minHeight.dp.roundToPx().toFloat()
    }

    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }
    var newPaddingTop by remember { mutableStateOf(0f) }
    var newHeight by remember { mutableStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.value + delta
                toolbarOffsetHeightPx.value = newOffset.coerceIn(toolbarMinHeightPx-toolbarHeightPx, 0f)
                return Offset.Zero
            }
        }
    }

    LaunchedEffect(key1 = toolbarOffsetHeightPx.value){
        newHeight = ((toolbarHeightPx + toolbarOffsetHeightPx.value) / density)
        newPaddingTop = if (newHeight == 0f) {
            0f
        } else {
            8f
        }
    }


    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(state.pullToRefresh) {
        if(state.pullToRefresh) {
            pullToRefreshState.startRefresh()
        } else {
            pullToRefreshState.endRefresh()
        }
    }

    if(pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            viewModel.pullToRefresh()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
    ) {
        PullToRefreshContainer(
            state = pullToRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
        Column(
            modifier = Modifier.fillMaxWidth()
                .nestedScroll(nestedScrollConnection)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PaddingValues(start = 16.dp, end = 16.dp, top = newPaddingTop.dp, bottom = 0.dp))
                    .height(newHeight.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                SearchTextField(
                    text = state.query,
                    onTextChanged = viewModel::onQueryChanged,
                    onSearch = viewModel::onSearch,
                    modifier = Modifier.weight(1f),
                    placeholder = "Search Pokemon"
                )
                Image(
                    painter = painterResource(id = R.drawable.search_pokemon),
                    contentDescription = "Search Icon",
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Crop
                )
            }

            when {
                (state.isLoading && state.items.isEmpty()) || state.searching -> {
                    GridShimmer(totalElements = 12)
                }
                !state.error.isNullOrEmpty() -> {
                    ErrorScreen(message = state.error)
                    onError()
                }
                else -> {
                    LazyVerticalGrid(
                        state = lazyListState,
                        columns = GridCells.Fixed(gridColumns),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(16.dp),
                    ) {
                        items(
                            state.items.size,
                            key = {
                                state.items[it].id
                            }
                        ) { i ->
                            val item = state.items[i]
                            if (i >= state.items.size - 1 && !state.endReached && !state.isLoading && !state.searchingWasFound) {
                                viewModel.loadNextItems()
                            }
                            CardPokemon(
                                pokemon = item,
                                cardColor = item.containerColor,
                                textColor = item.contentColor,
                                animatedVisibilityScope = animatedVisibilityScope,
                                onPokemonClick = {
                                    val image = item.imageUrl.replaceWithSharp()
                                    onNavigateTo(Routes.POKEMON_SCREEN + "/${item.name}/$image/${item.number}/${item.containerColor.toArgb()}")
                                }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        item(
                            span = { GridItemSpan(gridColumns) }
                        ) {
                            if (state.isLoading && state.items.isNotEmpty()) {
                                Box(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
