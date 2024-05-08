package com.github.jesushzc.pokedex.presentation.ui.home

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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.jesushzc.pokedex.presentation.components.CardPokemon
import com.github.jesushzc.pokedex.presentation.components.CustomScaffold
import com.github.jesushzc.pokedex.presentation.components.ErrorScreen
import com.github.jesushzc.pokedex.presentation.components.GridShimmer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {

    val state = viewModel.state
    val coroutineScope = rememberCoroutineScope()
    val lazyListState = rememberLazyGridState()

    var showFloatButton by rememberSaveable {
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
                lazyListState.animateScrollToItem(0, 0)
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
                    viewModel = viewModel
                )
            }
        }
    }

}

@Composable
private fun HomeContent(
    lazyListState: LazyGridState,
    viewModel: HomeViewModel
) {
    val state = viewModel.state
    val gridColumns = 2
    LazyVerticalGrid(
        state = lazyListState,
        columns = GridCells.Fixed(gridColumns),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(state.items.size) { i ->
            val item = state.items[i]
            if (i >= state.items.size - 1 && !state.endReached && !state.isLoading) {
                viewModel.loadNextItems()
            }
            var backgroundColor by remember { mutableStateOf(Color.White) }
            var textColor by remember { mutableStateOf(Color.Black) }
            viewModel.fetchColors(
                url = item.imageUrl,
                dominantColor = { background, text ->
                    backgroundColor = background
                    textColor = text
                }
            )
            CardPokemon(
                pokemon = item,
                cardColor = backgroundColor,
                textColor = textColor
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
