package com.github.jesushzc.pokedex.data.local.entity

import com.github.jesushzc.pokedex.utils.Constants
import io.realm.kotlin.types.EmbeddedRealmObject

class PokemonDetailsEntity: EmbeddedRealmObject {

    var weight: Int = 0
    var height: Int = 0
    var ability: String = Constants.EMPTY_STRING
    var category: String = Constants.EMPTY_STRING
    var color: Int = -1

}
