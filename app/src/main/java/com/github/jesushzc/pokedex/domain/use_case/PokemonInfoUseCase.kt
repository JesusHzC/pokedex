package com.github.jesushzc.pokedex.domain.use_case

import com.github.jesushzc.pokedex.domain.model.Pokemon
import com.github.jesushzc.pokedex.utils.Resource

interface PokemonInfoUseCase {

    suspend operator fun invoke(name: String): Resource<Pokemon>

}
