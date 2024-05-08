package com.github.jesushzc.pokedex.presentation.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.github.jesushzc.pokedex.presentation.components.CardPokemon
import com.github.jesushzc.pokedex.presentation.components.ErrorScreen
import com.github.jesushzc.pokedex.presentation.components.InverseGridShimmer

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {

    val state = viewModel.state

    when {
        state.isLoading && state.items.isEmpty() -> {
            InverseGridShimmer(totalElements = 12)
        }
        !state.error.isNullOrEmpty() -> {
            ErrorScreen()
        }
        else -> {
            LazyColumn(contentPadding = PaddingValues(16.dp)) {
                items(viewModel.state.items.size) { i ->
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
                item {
                    if (state.isLoading && state.items.isNotEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }

}
