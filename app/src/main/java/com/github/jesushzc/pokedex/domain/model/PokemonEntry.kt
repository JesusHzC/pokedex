package com.github.jesushzc.pokedex.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import java.util.UUID

@Immutable
data class PokemonEntry(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val imageUrl: String,
    val number: Int,
    var details: Pokemon? = null,
    val containerColor: Color = Color.White,
    val contentColor: Color = Color.Black
)
