package com.github.jesushzc.pokedex.presentation.ui.pokemon

import com.github.jesushzc.pokedex.domain.model.Characteristic
import com.github.jesushzc.pokedex.domain.model.Pokemon

data class PokemonState(
    val pokemon: Pokemon? = null,
    val error: String? = null,
    val characteristic: Characteristic? = null,
    val isFavorite: Boolean = false
)
