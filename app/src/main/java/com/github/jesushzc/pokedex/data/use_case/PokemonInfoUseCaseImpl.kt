package com.github.jesushzc.pokedex.data.use_case

import com.github.jesushzc.pokedex.domain.model.Pokemon
import com.github.jesushzc.pokedex.domain.repository.PokemonRepository
import com.github.jesushzc.pokedex.domain.use_case.PokemonInfoUseCase
import com.github.jesushzc.pokedex.utils.Resource

class PokemonInfoUseCaseImpl(
    private val repository: PokemonRepository
): PokemonInfoUseCase {

    override suspend fun invoke(name: String): Resource<Pokemon> {
        return repository.getPokemonInfo(name)
    }

}
