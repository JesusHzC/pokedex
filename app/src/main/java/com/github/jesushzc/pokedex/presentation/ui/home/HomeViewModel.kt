package com.github.jesushzc.pokedex.presentation.ui.home

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.github.jesushzc.pokedex.di.DefaultDispatcher
import com.github.jesushzc.pokedex.di.IoDispatcher
import com.github.jesushzc.pokedex.domain.model.PokemonEntry
import com.github.jesushzc.pokedex.domain.use_case.PokemonInfoUseCase
import com.github.jesushzc.pokedex.domain.use_case.PokemonListUseCase
import com.github.jesushzc.pokedex.utils.Constants.API_ERROR_MESSAGE
import com.github.jesushzc.pokedex.utils.Constants.EMPTY_STRING
import com.github.jesushzc.pokedex.utils.Constants.NUMBER_POKEMON_KEY
import com.github.jesushzc.pokedex.utils.Constants.PAGE_SIZE
import com.github.jesushzc.pokedex.utils.Constants.URL_IMAGE_POKEMON
import com.github.jesushzc.pokedex.utils.DefaultPaginator
import com.github.jesushzc.pokedex.utils.Resource
import com.github.jesushzc.pokedex.utils.calcDominantColors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pokemonListUseCase: PokemonListUseCase,
    private val pokemonInfoUseCase: PokemonInfoUseCase,
    private val appContext: Context,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private var paginator: DefaultPaginator<Int, PokemonEntry>? = null

    var state by mutableStateOf(HomeState())
        private set

    private var backupList: List<PokemonEntry> = emptyList()

    init {
        initPaginator()
        loadNextItems()
    }

    private fun initPaginator() {
        paginator = DefaultPaginator(
            initialKey = state.page,
            onLoadUpdated = {
                state = state.copy(isLoading = it)
            },
            onRequest = { nextPage ->
                getNextPage(nextPage)
            },
            getNextKey = {
                state.page + 1
            },
            onError = {
                state = state.copy(error = it?.localizedMessage)
                if (state.pullToRefresh)
                    state = state.copy(pullToRefresh = false)
            },
            onSuccess = { items, newKey ->
                state = state.copy(
                    items = state.items + items,
                    page = newKey,
                    endReached = items.isEmpty()
                )
                if (state.pullToRefresh)
                    state = state.copy(pullToRefresh = false)
                backupList = state.items
            }
        )
    }

    private suspend fun getNextPage(nextPage: Int): Result<List<PokemonEntry>> {
        return when (val result = pokemonListUseCase.invoke(PAGE_SIZE, nextPage * PAGE_SIZE)) {
            is Resource.Error -> {
                state = state.copy(error = API_ERROR_MESSAGE)
                Result.failure(Exception(result.message ?: API_ERROR_MESSAGE))
            }

            is Resource.Success -> {
                val resultApi = result.data
                val pokemonEntries = resultApi?.results?.mapIndexed { index, entry ->
                    val number = if (entry.url.endsWith("/")) {
                        entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                    } else {
                        entry.url.takeLastWhile { it.isDigit() }
                    }
                    val url = URL_IMAGE_POKEMON.replace(NUMBER_POKEMON_KEY, number)
                    val (backgroundColor, textColor) = fetchColorsAsync(url)
                    PokemonEntry(
                        number = (nextPage * PAGE_SIZE) + index + 1,
                        name = entry.name.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.ROOT
                            ) else it.toString()
                        },
                        imageUrl = url,
                        containerColor = backgroundColor ?: Color.White,
                        contentColor = textColor ?: Color.Black
                    )
                } ?: emptyList()
                Result.success(pokemonEntries)
            }
        }
    }

    fun loadNextItems() {
        viewModelScope.launch(defaultDispatcher) {
            paginator?.loadNextItems()
        }
    }

    private suspend fun fetchColorsAsync(url: String): Pair<Color?, Color?> = suspendCoroutine { continuation ->
        fetchColors(url) { background, text ->
            continuation.resume(Pair(background, text))
        }
    }

    private fun fetchColors(
        url: String,
        dominantColor: (Color, Color) -> Unit
    ) {
        viewModelScope.launch(ioDispatcher) {
            // Requesting the image using coil's ImageRequest
            val req = ImageRequest.Builder(appContext)
                .data(url)
                .allowHardware(false)
                .build()

            val result = req.context.imageLoader.execute(req)

            if (result is SuccessResult) {
                // Save the drawable as a state in order to use it on the composable
                // Converting it to bitmap and using it to calculate the palette
                calcDominantColors(result.drawable) { backgroundColor, textColor ->
                    dominantColor(backgroundColor, textColor)
                }
            } else {
                dominantColor(Color.White, Color.Black)
            }
        }
    }

    fun onQueryChanged(query: String) {
        state = state.copy(query = query.lowercase())
        if (query.isEmpty())
            state = state.copy(
                items = backupList,
                searchingWasFound = false,
                error = null
            )
    }

    fun onSearch() {
        val query = state.query

        if (query.isEmpty())
            return

        state = state.copy(
            searching = true
        )

        val findItem = backupList.find { it.name == query }

        if (findItem != null) {
            state = state.copy(
                items = listOf(findItem),
                searching = false,
                searchingWasFound = true
            )
            return
        }

        viewModelScope.launch(defaultDispatcher) {
            when (val result = pokemonInfoUseCase.invoke(query)) {
                is Resource.Success -> {
                    val url = URL_IMAGE_POKEMON.replace(NUMBER_POKEMON_KEY, result.data?.id.toString())
                    val (backgroundColor, textColor) = fetchColorsAsync(url)
                    state = state.copy(
                        items = listOf(
                            PokemonEntry(
                                number = result.data?.id ?: 0,
                                name = result.data?.name?.replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(
                                        Locale.ROOT
                                    ) else it.toString()
                                } ?: EMPTY_STRING,
                                imageUrl = url,
                                containerColor = backgroundColor ?: Color.White,
                                contentColor = textColor ?: Color.Black
                            )
                        ),
                        searching = false,
                        searchingWasFound = true
                    )
                }
                is Resource.Error -> {
                    state = state.copy(
                        items = backupList,
                        error = "Pokemon not found",
                        searching = false
                    )
                }
            }
        }
    }

    fun pullToRefresh() {
        state = state.copy(
            pullToRefresh = true,
            items = emptyList(),
            page = 0,
            endReached = false
        )
        initPaginator()
        loadNextItems()
    }

}
