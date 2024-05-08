package com.github.jesushzc.pokedex.data.use_case

import com.github.jesushzc.pokedex.domain.model.PokemonList
import com.github.jesushzc.pokedex.domain.repository.PokemonRepository
import com.github.jesushzc.pokedex.domain.use_case.PokemonListUseCase
import com.github.jesushzc.pokedex.utils.Resource

class PokemonListUseCaseImpl(
    private val repository: PokemonRepository
): PokemonListUseCase {

    override suspend fun invoke(limit: Int, offset: Int): Resource<PokemonList> {
        return repository.getPokemonList(limit, offset)
    }

}
