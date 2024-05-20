package com.github.jesushzc.pokedex.presentation.ui.favorites

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jesushzc.pokedex.data.local.dao.PokemonDao
import com.github.jesushzc.pokedex.data.local.entity.PokemonEntity
import com.github.jesushzc.pokedex.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val pokemonDao: PokemonDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): ViewModel() {

    var favorites by mutableStateOf(emptyList<PokemonEntity>())
        private set

    fun getFavorites() {
        viewModelScope.launch(ioDispatcher) {
            favorites = pokemonDao.getAllPokemons()
        }
    }

    fun deleteFavorite(pokemon: PokemonEntity) {
        viewModelScope.launch(ioDispatcher) {
            pokemonDao.deletePokemon(pokemon)
            getFavorites()
        }
    }

}