package com.github.jesushzc.pokedex.domain.use_case

import com.github.jesushzc.pokedex.domain.model.PokemonList
import com.github.jesushzc.pokedex.utils.Network

interface PokemonListUseCase {

    suspend operator fun invoke(limit: Int, offset: Int): Network<PokemonList>

}
