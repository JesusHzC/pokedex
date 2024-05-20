package com.github.jesushzc.pokedex.data.local.dao

import com.github.jesushzc.pokedex.data.local.entity.PokemonEntity

interface PokemonDao {

    suspend fun getAllPokemons(): List<PokemonEntity>

    suspend fun getPokemonById(id: Int): PokemonEntity?

    suspend fun insertPokemon(pokemon: PokemonEntity)

    suspend fun deletePokemon(pokemon: PokemonEntity)

    suspend fun deleteAllPokemons()

}
