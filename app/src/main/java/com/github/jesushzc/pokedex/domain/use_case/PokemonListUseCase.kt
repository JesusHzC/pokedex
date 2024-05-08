package com.github.jesushzc.pokedex.domain.use_case

import com.github.jesushzc.pokedex.domain.model.PokemonList
import com.github.jesushzc.pokedex.utils.Resource

interface PokemonListUseCase {

    suspend operator fun invoke(limit: Int, offset: Int): Resource<PokemonList>

}
