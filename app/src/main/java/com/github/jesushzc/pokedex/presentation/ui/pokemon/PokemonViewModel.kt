package com.github.jesushzc.pokedex.presentation.ui.pokemon

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jesushzc.pokedex.data.local.dao.PokemonDao
import com.github.jesushzc.pokedex.data.local.entity.PokemonDetailsEntity
import com.github.jesushzc.pokedex.data.local.entity.PokemonEntity
import com.github.jesushzc.pokedex.data.local.entity.PokemonType
import com.github.jesushzc.pokedex.di.IoDispatcher
import com.github.jesushzc.pokedex.domain.use_case.PokemonCharacteristicUseCase
import com.github.jesushzc.pokedex.domain.use_case.PokemonInfoUseCase
import com.github.jesushzc.pokedex.utils.Constants
import com.github.jesushzc.pokedex.utils.Constants.API_ERROR_MESSAGE
import com.github.jesushzc.pokedex.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val pokemonInfoUseCase: PokemonInfoUseCase,
    private val pokemonCharacteristicUseCase: PokemonCharacteristicUseCase,
    private val pokemonDao: PokemonDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): ViewModel() {

    var state by mutableStateOf(PokemonState())
        private set

    private var imageUrl: String = Constants.EMPTY_STRING
    private var pokemonId: Int = -1
    private var color: Int = -1

    fun setImageUrl(url: String) {
        imageUrl = url
    }

    fun setPokemonId(id: Int) {
        pokemonId = id
    }

    fun setColor(color: Int) {
        this.color = color
    }

    fun getPokemonInfo(pokemonName: String) {
        viewModelScope.launch {
            state = when (val result = pokemonInfoUseCase(pokemonName)) {
                is Resource.Success -> {
                    state.copy(
                        pokemon = result.data
                    )
                }

                is Resource.Error -> {
                    state.copy(
                        error = result.message ?: API_ERROR_MESSAGE
                    )
                }
            }
        }
    }

    fun getCharacteristics(pokemonId: Int) {
        viewModelScope.launch {
            state = when (val result = pokemonCharacteristicUseCase(pokemonId)) {
                is Resource.Success -> {
                    state.copy(
                        characteristic = result.data
                    )
                }

                is Resource.Error -> {
                    state.copy(
                        error = result.message ?: API_ERROR_MESSAGE
                    )
                }
            }
        }
    }

    fun setFavorite() {
        viewModelScope.launch(ioDispatcher) {
            state = state.copy(
                isFavorite = findPokemonOnDb() != null
            )
        }
    }

    private suspend fun findPokemonOnDb(): PokemonEntity? {
        return pokemonDao.getPokemonById(pokemonId)
    }

    fun addOrRemoveFavorite() = viewModelScope.launch(ioDispatcher) {
        when {
            state.isFavorite -> {
                findPokemonOnDb()?.let {
                    pokemonDao.deletePokemon(it)
                    setFavorite()
                }
            }
            else -> {
                val pokemonDetailsEntity = PokemonDetailsEntity().apply {
                    weight = state.pokemon?.weight ?: 0
                    height = state.pokemon?.height ?: 0
                    ability = state.pokemon?.abilities?.first()?.ability?.name ?: Constants.EMPTY_STRING
                    category = state.pokemon?.species?.name ?: Constants.EMPTY_STRING
                    color = this@PokemonViewModel.color
                }
                val pokemonEntity = PokemonEntity().apply {
                    _id = pokemonId
                    name = state.pokemon?.name ?: Constants.EMPTY_STRING
                    imageUrl = this@PokemonViewModel.imageUrl
                    details = pokemonDetailsEntity
                }
                state.pokemon?.types?.forEach {
                    val pokemonType = PokemonType().apply {
                        this.name = it.type?.name ?: Constants.EMPTY_STRING
                    }
                    pokemonEntity.types.add(pokemonType)
                }
                pokemonDao.insertPokemon(pokemonEntity)
                setFavorite()
            }
        }
    }

}
