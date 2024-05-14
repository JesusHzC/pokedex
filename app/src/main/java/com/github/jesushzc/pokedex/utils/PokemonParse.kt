package com.github.jesushzc.pokedex.utils

import androidx.compose.ui.graphics.Color
import com.github.jesushzc.pokedex.R
import com.github.jesushzc.pokedex.domain.model.Stat
import com.github.jesushzc.pokedex.domain.model.Stats
import com.github.jesushzc.pokedex.domain.model.Type
import com.github.jesushzc.pokedex.presentation.theme.AtkColor
import com.github.jesushzc.pokedex.presentation.theme.DefColor
import com.github.jesushzc.pokedex.presentation.theme.HPColor
import com.github.jesushzc.pokedex.presentation.theme.SpAtkColor
import com.github.jesushzc.pokedex.presentation.theme.SpDefColor
import com.github.jesushzc.pokedex.presentation.theme.SpdColor
import com.github.jesushzc.pokedex.presentation.theme.TypeBug
import com.github.jesushzc.pokedex.presentation.theme.TypeDark
import com.github.jesushzc.pokedex.presentation.theme.TypeDragon
import com.github.jesushzc.pokedex.presentation.theme.TypeElectric
import com.github.jesushzc.pokedex.presentation.theme.TypeFairy
import com.github.jesushzc.pokedex.presentation.theme.TypeFighting
import com.github.jesushzc.pokedex.presentation.theme.TypeFire
import com.github.jesushzc.pokedex.presentation.theme.TypeFlying
import com.github.jesushzc.pokedex.presentation.theme.TypeGhost
import com.github.jesushzc.pokedex.presentation.theme.TypeGrass
import com.github.jesushzc.pokedex.presentation.theme.TypeGround
import com.github.jesushzc.pokedex.presentation.theme.TypeIce
import com.github.jesushzc.pokedex.presentation.theme.TypeNormal
import com.github.jesushzc.pokedex.presentation.theme.TypePoison
import com.github.jesushzc.pokedex.presentation.theme.TypePsychic
import com.github.jesushzc.pokedex.presentation.theme.TypeRock
import com.github.jesushzc.pokedex.presentation.theme.TypeSteel
import com.github.jesushzc.pokedex.presentation.theme.TypeWater
import java.util.*

fun parseTypeToColor(type: Type): Color {
    return when(type.name!!.lowercase(Locale.ROOT)) {
        "normal" -> TypeNormal
        "fire" -> TypeFire//
        "water" -> TypeWater//
        "electric" -> TypeElectric//
        "grass" -> TypeGrass
        "ice" -> TypeIce//
        "fighting" -> TypeFighting
        "poison" -> TypePoison
        "ground" -> TypeGround
        "flying" -> TypeFlying
        "psychic" -> TypePsychic
        "bug" -> TypeBug
        "rock" -> TypeRock
        "ghost" -> TypeGhost//
        "dragon" -> TypeDragon//
        "dark" -> TypeDark
        "steel" -> TypeSteel
        "fairy" -> TypeFairy//
        else -> Color.Black
    }
}

fun parseTypeToImage(type: Type): Int {
    return when(type.name!!.lowercase(Locale.ROOT)) {
        "normal" -> R.drawable.normal
        "fire" -> R.drawable.fire
        "water" -> R.drawable.water
        "electric" -> R.drawable.electric
        "grass" -> R.drawable.grass
        "ice" -> R.drawable.ice
        "fighting" -> R.drawable.fighting
        "poison" -> R.drawable.poison
        "ground" -> R.drawable.ground
        "flying" -> R.drawable.flying
        "psychic" -> R.drawable.psychic
        "bug" -> R.drawable.bug
        "rock" -> R.drawable.rock
        "ghost" -> R.drawable.ghost
        "dragon" -> R.drawable.dragon
        "dark" -> R.drawable.dark
        "steel" -> R.drawable.steel
        "fairy" -> R.drawable.fairy
        else -> R.drawable.normal
    }
}

fun parseStatToColor(stat: Stats): Color {
    return when(stat.stat!!.name!!.lowercase()) {
        "hp" -> HPColor
        "attack" -> AtkColor
        "defense" -> DefColor
        "special-attack" -> SpAtkColor
        "special-defense" -> SpDefColor
        "speed" -> SpdColor
        else -> Color.White
    }
}

fun parseStatToAbbr(stat: Stats): String {
    return when(stat.stat!!.name!!.lowercase()) {
        "hp" -> "HP"
        "attack" -> "Atk"
        "defense" -> "Def"
        "special-attack" -> "SpAtk"
        "special-defense" -> "SpDef"
        "speed" -> "Spd"
        else -> ""
    }
}