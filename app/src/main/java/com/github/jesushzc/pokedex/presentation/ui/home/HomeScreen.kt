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
        showFloatingButton = showFloatButton,
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

@OptIn(ExperimentalSharedTransitionApi::class)
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingValues(horizontal = 16.dp, vertical = 12.dp)),
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
                ErrorScreen(state.error)
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
