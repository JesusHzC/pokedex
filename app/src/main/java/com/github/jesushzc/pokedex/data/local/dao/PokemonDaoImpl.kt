package com.github.jesushzc.pokedex.data.local.dao

import com.github.jesushzc.pokedex.data.local.entity.PokemonEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import javax.inject.Inject

class PokemonDaoImpl @Inject constructor(
    private val db: Realm
): PokemonDao {

    override suspend fun getAllPokemons(): List<PokemonEntity> {
        return db.query(PokemonEntity::class).find()
    }

    override suspend fun getPokemonById(id: Int): PokemonEntity? {
        return db.query<PokemonEntity>("_id == $0", id).first().find()
    }

    override suspend fun insertPokemon(pokemon: PokemonEntity) {
        db.write {
            copyToRealm(pokemon)
        }
    }

    override suspend fun deletePokemon(pokemon: PokemonEntity) {
        db.write {
            val livePokemon = findLatest(pokemon)
            if (livePokemon != null) {
                delete(livePokemon)
            }
        }
    }

    override suspend fun deleteAllPokemons() {
        db.write {
            val allPokemons = query(PokemonEntity::class).find()
            allPokemons.forEach { pokemon ->
                val livePokemon = findLatest(pokemon)
                if (livePokemon != null) {
                    delete(livePokemon)
                }
            }
        }
    }

}
