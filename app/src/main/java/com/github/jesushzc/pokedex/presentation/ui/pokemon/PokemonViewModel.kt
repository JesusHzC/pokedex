package com.github.jesushzc.pokedex.presentation.ui.pokemon

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jesushzc.pokedex.domain.use_case.PokemonCharacteristicUseCase
import com.github.jesushzc.pokedex.domain.use_case.PokemonInfoUseCase
import com.github.jesushzc.pokedex.utils.Constants.API_ERROR_MESSAGE
import com.github.jesushzc.pokedex.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val pokemonInfoUseCase: PokemonInfoUseCase,
    private val pokemonCharacteristicUseCase: PokemonCharacteristicUseCase
): ViewModel() {

    var state by mutableStateOf(PokemonState())
        private set

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

}
