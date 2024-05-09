package com.github.jesushzc.pokedex.presentation.ui.pokemon

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.github.jesushzc.pokedex.domain.model.Types
import com.github.jesushzc.pokedex.presentation.components.ErrorScreen
import com.github.jesushzc.pokedex.utils.Constants.EMPTY_STRING
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
    LaunchedEffect(key1 = true) {
        viewModel.getPokemonInfo(
            pokemonName.replaceFirstChar { it.lowercase() }
        )
    }

    val state = viewModel.state

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
    Column {
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
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .sharedElement(
                        state = rememberSharedContentState(key = "name/$pokemonName"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween(durationMillis = 1000)
                        }
                    )
            )
            Text(
                text = "N° $pokemonNumber",
                fontSize = 18.sp,
                modifier = Modifier
                    .sharedElement(
                        state = rememberSharedContentState(key = "number/$pokemonNumber"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween(durationMillis = 1000)
                        }
                    )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                state.pokemon?.types?.forEach {
                    val type = it.type!!
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
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


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
