package com.github.jesushzc.pokedex.domain.model

import com.squareup.moshi.Json

data class Characteristic(
    @Json(name = "descriptions") var descriptions: List<Descriptions> = emptyList(),
    @Json(name = "gene_modulo") var geneModulo: Int? = null,
    @Json(name = "highest_stat") var highestStat: HighestStat? = HighestStat(),
    @Json(name = "id") var id: Int? = null,
    @Json(name = "possible_values") var possibleValues: List<Int> = emptyList()
)

data class Language(

    @Json(name = "name") var name: String? = null,
    @Json(name = "url") var url: String? = null

)

data class Descriptions(

    @Json(name = "description") var description: String? = null,
    @Json(name = "language") var language: Language? = Language()

)

data class HighestStat(

    @Json(name = "name") var name: String? = null,
    @Json(name = "url") var url: String? = null

)
