package com.github.jesushzc.pokedex.di

import com.github.jesushzc.pokedex.data.use_case.PokemonCharacteristicUseCaseImpl
import com.github.jesushzc.pokedex.data.use_case.PokemonInfoUseCaseImpl
import com.github.jesushzc.pokedex.data.use_case.PokemonListUseCaseImpl
import com.github.jesushzc.pokedex.domain.repository.PokemonRepository
import com.github.jesushzc.pokedex.domain.use_case.PokemonCharacteristicUseCase
import com.github.jesushzc.pokedex.domain.use_case.PokemonInfoUseCase
import com.github.jesushzc.pokedex.domain.use_case.PokemonListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @ViewModelScoped
    fun providesGetPokemonListUseCase(
        repository: PokemonRepository
    ): PokemonListUseCase {
        return PokemonListUseCaseImpl(repository)
    }

    @Provides
    @ViewModelScoped
    fun providesGetPokemonInfoUseCase(
        repository: PokemonRepository
    ): PokemonInfoUseCase {
        return PokemonInfoUseCaseImpl(repository)
    }

    @Provides
    @ViewModelScoped
    fun providesGetPokemonCharacteristicUseCase(
        repository: PokemonRepository
    ): PokemonCharacteristicUseCase {
        return PokemonCharacteristicUseCaseImpl(repository)
    }

}
