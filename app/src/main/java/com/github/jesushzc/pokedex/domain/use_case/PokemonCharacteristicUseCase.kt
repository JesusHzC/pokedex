package com.github.jesushzc.pokedex.domain.use_case

import com.github.jesushzc.pokedex.domain.model.Characteristic
import com.github.jesushzc.pokedex.utils.Resource

interface PokemonCharacteristicUseCase {

    suspend operator fun invoke(id: Int): Resource<Characteristic>

}
