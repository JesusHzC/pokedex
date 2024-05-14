package com.github.jesushzc.pokedex.domain.repository

import com.github.jesushzc.pokedex.domain.model.Characteristic
import com.github.jesushzc.pokedex.domain.model.Pokemon
import com.github.jesushzc.pokedex.domain.model.PokemonList
import com.github.jesushzc.pokedex.utils.Resource

interface PokemonRepository {

    suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonList>

    suspend fun getPokemonInfo(name: String): Resource<Pokemon>

    suspend fun getPokemonCharacteristic(id: Int): Resource<Characteristic>

}
