package com.github.jesushzc.pokedex.data.local.entity

import com.github.jesushzc.pokedex.utils.Constants
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

class PokemonEntity: RealmObject {

    @PrimaryKey var _id: Int = 0
    var name: String = Constants.EMPTY_STRING
    var imageUrl: String = Constants.EMPTY_STRING
    var details: PokemonDetailsEntity? = null
    var types: RealmList<PokemonType> = realmListOf()

}
