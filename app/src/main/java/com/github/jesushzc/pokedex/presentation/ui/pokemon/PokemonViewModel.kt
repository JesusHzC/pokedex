package com.github.jesushzc.pokedex.presentation.ui.pokemon

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jesushzc.pokedex.domain.use_case.PokemonInfoUseCase
import com.github.jesushzc.pokedex.utils.Constants.API_ERROR_MESSAGE
import com.github.jesushzc.pokedex.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonViewModel @Inject constructor(
    private val pokemonInfoUseCase: PokemonInfoUseCase
): ViewModel() {

    var state by mutableStateOf(PokemonState())
        private set

    fun getPokemonInfo(pokemonName: String) {
        viewModelScope.launch {
            when(val result = pokemonInfoUseCase(pokemonName)) {
                is Resource.Success -> {
                    state = PokemonState(
                        pokemon = result.data,
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    state = PokemonState(
                        error = result.message ?: API_ERROR_MESSAGE,
                        isLoading = false
                    )
                }
            }
        }
    }

}
