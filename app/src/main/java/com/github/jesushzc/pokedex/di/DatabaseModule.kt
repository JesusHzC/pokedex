package com.github.jesushzc.pokedex.di

import com.github.jesushzc.pokedex.data.local.dao.PokemonDao
import com.github.jesushzc.pokedex.data.local.dao.PokemonDaoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {

    @Binds
    abstract fun bindPokemonDao(pokemonDaoImpl: PokemonDaoImpl): PokemonDao

}
