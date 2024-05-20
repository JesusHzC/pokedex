package com.github.jesushzc.pokedex.di

import android.app.Application
import android.content.Context
import com.github.jesushzc.pokedex.data.local.entity.PokemonDetailsEntity
import com.github.jesushzc.pokedex.data.local.entity.PokemonEntity
import com.github.jesushzc.pokedex.data.local.entity.PokemonType
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesAppContext(app: Application): Context {
        return app.applicationContext
    }

    @Provides
    @Singleton
    fun providesRealmConfiguration(): RealmConfiguration {
        return RealmConfiguration.Builder(
            schema = setOf(
                PokemonEntity::class,
                PokemonDetailsEntity::class,
                PokemonType::class
            )
        ).schemaVersion(4)
            .deleteRealmIfMigrationNeeded()
            .build()
    }

    @Provides
    @Singleton
    fun providesPokedexDatabase(
        configuration: RealmConfiguration
    ): Realm {
        return Realm.open(
            configuration = configuration
        )
    }

}
