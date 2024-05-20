package com.github.jesushzc.pokedex.data.local.entity

import com.github.jesushzc.pokedex.utils.Constants.EMPTY_STRING
import io.realm.kotlin.types.EmbeddedRealmObject

class PokemonType: EmbeddedRealmObject {
    var name: String = EMPTY_STRING
}