package com.github.jesushzc.pokedex.domain.model

import com.github.jesushzc.pokedex.utils.Constants.EMPTY_STRING
import com.squareup.moshi.Json

data class PokemonList(
    @Json(name = "count"    ) var count    : Int,
    @Json(name = "next"     ) var next     : String,
    @Json(name = "previous" ) var previous : String,
    @Json(name = "results"  ) var results  : List<Results> = emptyList()
)

data class Results (
    @Json(name = "name" ) var name : String = EMPTY_STRING,
    @Json(name = "url"  ) var url  : String = EMPTY_STRING
)
