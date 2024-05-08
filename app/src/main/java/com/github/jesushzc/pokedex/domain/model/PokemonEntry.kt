package com.github.jesushzc.pokedex.domain.model

data class PokemonEntry(
    val name: String,
    val imageUrl: String,
    val number: Int,
    var details: Pokemon? = null
)
