package com.github.jesushzc.pokedex.presentation.ui.home

import com.github.jesushzc.pokedex.domain.model.PokemonEntry

data class HomeState(
    val isLoading: Boolean = true,
    val items: List<PokemonEntry> = emptyList(),
    val error: String? = null,
    val endReached: Boolean = false,
    val page: Int = 0,
    val query: String = "",
    val searching: Boolean = false,
    val searchingWasFound: Boolean = false,
    val pullToRefresh: Boolean = false
)
