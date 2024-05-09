package com.github.jesushzc.pokedex.presentation.ui.pokemon

import com.github.jesushzc.pokedex.domain.model.Pokemon

data class PokemonState(
    val isLoading: Boolean = false,
    val pokemon: Pokemon? = null,
    val error: String? = null
)
