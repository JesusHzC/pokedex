package com.github.jesushzc.pokedex.domain.repository

import com.github.jesushzc.pokedex.domain.model.Characteristic
import com.github.jesushzc.pokedex.domain.model.Pokemon
import com.github.jesushzc.pokedex.domain.model.PokemonList
import com.github.jesushzc.pokedex.utils.Network

interface PokemonRepository {

    suspend fun getPokemonList(limit: Int, offset: Int): Network<PokemonList>

    suspend fun getPokemonInfo(name: String): Network<Pokemon>

    suspend fun getPokemonCharacteristic(id: Int): Network<Characteristic>

}
