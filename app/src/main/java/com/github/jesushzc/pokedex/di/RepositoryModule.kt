package com.github.jesushzc.pokedex.di

import com.github.jesushzc.pokedex.data.remote.PokeApi
import com.github.jesushzc.pokedex.data.repository.PokemonRepositoryImpl
import com.github.jesushzc.pokedex.domain.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providesPokemonRepository(
        pokeApi: PokeApi
    ): PokemonRepository {
        return PokemonRepositoryImpl(pokeApi)
    }

}
