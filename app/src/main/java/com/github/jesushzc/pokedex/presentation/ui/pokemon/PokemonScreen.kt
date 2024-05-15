package com.github.jesushzc.pokedex.presentation.ui.pokemon

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.github.jesushzc.pokedex.R
import com.github.jesushzc.pokedex.domain.model.Pokemon
import com.github.jesushzc.pokedex.domain.model.Type
import com.github.jesushzc.pokedex.domain.model.Types
import com.github.jesushzc.pokedex.presentation.components.ErrorScreen
import com.github.jesushzc.pokedex.utils.Constants.EMPTY_STRING
import com.github.jesushzc.pokedex.utils.parseStatToAbbr
import com.github.jesushzc.pokedex.utils.parseStatToColor
import com.github.jesushzc.pokedex.utils.parseTypeToColor
import com.github.jesushzc.pokedex.utils.parseTypeToImage

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.PokemonScreen(
    viewModel: PokemonViewModel = hiltViewModel(),
    pokemonName: String,
    pokemonNumber: Int,
    pokemonImage: String,
    color: Int,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val state = viewModel.state

    LaunchedEffect(key1 = true) {
        viewModel.getPokemonInfo(
            pokemonName.replaceFirstChar { it.lowercase() }
        )
        state.pokemon?.id?.let { pokemonId ->
            viewModel.getCharacteristics(pokemonId)
        }
    }

    when {
        !state.error.isNullOrEmpty() -> {
            ErrorScreen()
        }
        else -> {
            PokemonContent(
                viewModel = viewModel,
                pokemonImage = pokemonImage,
                pokemonNumber = pokemonNumber,
                pokemonName = pokemonName,
                color = color,
                animatedVisibilityScope = animatedVisibilityScope
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.PokemonContent(
    viewModel: PokemonViewModel,
    pokemonImage: String,
    pokemonNumber: Int,
    pokemonName: String,
    color: Int,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val state = viewModel.state
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(bottom = 56.dp)
    ) {
        Header(
            imageUrl = pokemonImage,
            color = color,
            type = state.pokemon?.types?.first(),
            animatedVisibilityScope = animatedVisibilityScope
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = pokemonName,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "N° $pokemonNumber",
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                state.pokemon?.types?.forEach {
                    val type = it.type!!
                    CardType(type = type)
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            state.characteristic?.descriptions?.forEach { description ->
                if (description.language?.name == "en") {
                    Text(
                        text = description.description ?: EMPTY_STRING,
                        fontSize = 16.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Justify,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val characteristics = listOf(
                PokemonCharacteristic(
                    PokemonCharacteristicType.HEIGHT,
                    state.pokemon?.height?.toString() ?: EMPTY_STRING
                ),
                PokemonCharacteristic(
                    PokemonCharacteristicType.WEIGHT,
                    state.pokemon?.weight?.toString() ?: EMPTY_STRING
                ),
                PokemonCharacteristic(
                    PokemonCharacteristicType.ABILITY,
                    state.pokemon?.abilities?.first()?.ability?.name ?: EMPTY_STRING
                ),
                PokemonCharacteristic(
                    PokemonCharacteristicType.CATEGORY,
                    state.pokemon?.species?.name ?: EMPTY_STRING
                )
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.heightIn(max = 300.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(characteristics) { characteristic ->
                    CardCharacteristic(characteristic = characteristic)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Stats",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (state.pokemon != null) {
                val pokemon = state.pokemon
                val maxBaseStat = pokemon.stats.maxOf { it.baseStat!! }
                val animDelayPerItem = 100
                pokemon.stats.indices.forEach { index ->
                    val stat = pokemon.stats[index]
                    PokemonStat(
                        statName = parseStatToAbbr(stat),
                        statValue = stat.baseStat!!,
                        statMaxValue = maxBaseStat,
                        statColor = parseStatToColor(stat),
                        animDelay = index * animDelayPerItem
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun PokemonStat(
    statName: String,
    statValue: Int,
    statMaxValue: Int,
    statColor: Color,
    height: Dp = 28.dp,
    animDuration: Int = 1000,
    animDelay: Int = 0
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val curPercent = animateFloatAsState(
        targetValue = if(animationPlayed) {
            statValue / statMaxValue.toFloat()
        } else 0f,
        animationSpec = tween(
            animDuration,
            animDelay
        ), label = ""
    )
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(CircleShape)
            .background(
                Color.LightGray
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(curPercent.value)
                .clip(CircleShape)
                .background(statColor)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = statName,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = (curPercent.value * statMaxValue).toInt().toString(),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun CardType(type: Type) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(color = parseTypeToColor(type))
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(25.dp)
                    .clip(CircleShape)
                    .background(color = Color.White),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = parseTypeToImage(type)),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = type.name?.replaceFirstChar { it.uppercase() } ?: EMPTY_STRING,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

private enum class PokemonCharacteristicType(val icon: Int, val title: String) {
    HEIGHT(icon = R.drawable.height, title = "Height"),
    WEIGHT(icon = R.drawable.weight, title = "Weight"),
    ABILITY(icon = R.drawable.hability, title = "Ability"),
    CATEGORY(icon = R.drawable.category, title = "Category")
}

private data class PokemonCharacteristic(
    val characteristicType: PokemonCharacteristicType,
    val value: String
)

@Composable
private fun CardCharacteristic(
    characteristic: PokemonCharacteristic,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(id = characteristic.characteristicType.icon),
                contentDescription = null,
                modifier = Modifier.size(15.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = characteristic.characteristicType.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Light
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .width(150.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color = Color.White)
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = characteristic.value,
                fontSize = 16.sp,
                modifier = modifier.padding(12.dp),
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.Header(
    imageUrl: String,
    color: Int,
    type: Types? = null,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(370.dp)
    ) {
        Box(
            modifier = Modifier
                .height(280.dp)
                .fillMaxWidth()
                .clip(
                    shape = RoundedCornerShape(
                        bottomStart = 70.dp,
                        bottomEnd = 70.dp
                    )
                )
                .background(color = convertColor(color))
        ) {
            if (type != null) {
                Image(
                    painter = painterResource(id = parseTypeToImage(type.type!!)),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .size(200.dp)
                .sharedElement(
                    state = rememberSharedContentState(key = "image/$imageUrl"),
                    animatedVisibilityScope = animatedVisibilityScope,
                    boundsTransform = { _, _ ->
                        tween(durationMillis = 1000)
                    }
                ),
            contentScale = ContentScale.Crop
        )
    }
}

fun convertColor(color: Int): Color {
    val red = android.graphics.Color.red(color) / 255f
    val green = android.graphics.Color.green(color) / 255f
    val blue = android.graphics.Color.blue(color) / 255f
    val alpha = android.graphics.Color.alpha(color) / 255f

    return Color(red = red, green = green, blue = blue, alpha = alpha)
}
