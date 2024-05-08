package com.github.jesushzc.pokedex.data.repository

import android.util.Log
import com.github.jesushzc.pokedex.data.remote.PokeApi
import com.github.jesushzc.pokedex.domain.model.Pokemon
import com.github.jesushzc.pokedex.domain.model.PokemonList
import com.github.jesushzc.pokedex.domain.repository.PokemonRepository
import com.github.jesushzc.pokedex.utils.Constants.API_ERROR_LOG_TAG
import com.github.jesushzc.pokedex.utils.Constants.API_ERROR_MESSAGE
import com.github.jesushzc.pokedex.utils.Resource

class PokemonRepositoryImpl(
    private val pokeApi: PokeApi
): PokemonRepository {

    override suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonList> {
        return try {
            val response = pokeApi.getPokemonList(limit, offset)
            Resource.Success(response)
        } catch(e: Exception) {
            Log.e(API_ERROR_LOG_TAG, e.message ?: API_ERROR_MESSAGE)
            Resource.Error(API_ERROR_MESSAGE)
        }
    }

    override suspend fun getPokemonInfo(name: String): Resource<Pokemon> {
        return try {
            val response = pokeApi.getPokemonInfo(name)
            Resource.Success(response)
        } catch(e: Exception) {
            Log.e(API_ERROR_LOG_TAG, e.message ?: API_ERROR_MESSAGE)
            Resource.Error(API_ERROR_MESSAGE)
        }
    }

}
