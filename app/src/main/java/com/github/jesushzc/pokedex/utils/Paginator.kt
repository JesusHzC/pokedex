package com.github.jesushzc.pokedex.utils

interface Paginator<Key, Item> {

    suspend fun loadNextItems()
    fun reset()

}
