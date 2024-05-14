package com.github.jesushzc.pokedex.data.use_case

import com.github.jesushzc.pokedex.domain.model.Characteristic
import com.github.jesushzc.pokedex.domain.repository.PokemonRepository
import com.github.jesushzc.pokedex.domain.use_case.PokemonCharacteristicUseCase
import com.github.jesushzc.pokedex.utils.Resource

class PokemonCharacteristicUseCaseImpl(
    private val repository: PokemonRepository
): PokemonCharacteristicUseCase {

    override suspend fun invoke(id: Int): Resource<Characteristic> {
        return repository.getPokemonCharacteristic(id)
    }

}
