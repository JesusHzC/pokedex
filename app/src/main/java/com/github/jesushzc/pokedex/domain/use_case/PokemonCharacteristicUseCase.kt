package com.github.jesushzc.pokedex.domain.use_case

import com.github.jesushzc.pokedex.domain.model.Characteristic
import com.github.jesushzc.pokedex.utils.Network

interface PokemonCharacteristicUseCase {

    suspend operator fun invoke(id: Int): Network<Characteristic>

}
