package com.github.jesushzc.pokedex.presentation.ui.home

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.jesushzc.pokedex.presentation.components.CardPokemon
import com.github.jesushzc.pokedex.presentation.components.CustomScaffold
import com.github.jesushzc.pokedex.presentation.components.ErrorScreen
import com.github.jesushzc.pokedex.presentation.components.GridShimmer
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

    val state = viewModel.state
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
        when {
            state.isLoading && state.items.isEmpty() -> {
                GridShimmer(totalElements = 12)
            }
            !state.error.isNullOrEmpty() -> {
                ErrorScreen()
            }
            else -> {
                HomeContent(
                    lazyListState = lazyListState,
                    viewModel = viewModel,
                    animatedVisibilityScope = animatedVisibilityScope,
                    onNavigateTo = onNavigateTo
                )
            }
        }
    }

}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.HomeContent(
    lazyListState: LazyGridState,
    viewModel: HomeViewModel,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onNavigateTo: (String) -> Unit
) {
    val state = viewModel.state
    val gridColumns = 2
    LazyVerticalGrid(
        state = lazyListState,
        columns = GridCells.Fixed(gridColumns),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(
            state.items.size,
            key = {
                state.items[it].id
            }
        ) { i ->
            val item = state.items[i]
            if (i >= state.items.size - 1 && !state.endReached && !state.isLoading) {
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
