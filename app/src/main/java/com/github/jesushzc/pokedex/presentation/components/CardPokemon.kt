package com.github.jesushzc.pokedex.presentation.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.github.jesushzc.pokedex.domain.model.PokemonEntry
import java.util.Locale

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CardPokemon(
    pokemon: PokemonEntry,
    modifier: Modifier = Modifier,
    cardColor: Color,
    textColor: Color,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onPokemonClick: () -> Unit
) {
    PokemonContent(
        pokemon = pokemon,
        modifier = modifier,
        cardColor = cardColor,
        textColor = textColor,
        animatedVisibilityScope = animatedVisibilityScope,
        onPokemonClick = onPokemonClick
    )
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun SharedTransitionScope.PokemonContent(
    pokemon: PokemonEntry,
    modifier: Modifier,
    cardColor: Color,
    textColor: Color,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onPokemonClick: () -> Unit
) {
    Card(
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(
            containerColor = cardColor,
            contentColor = cardColor
        ),
        onClick = onPokemonClick
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = pokemon.imageUrl,
                contentDescription = pokemon.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(150.dp)
                    .height(80.dp)
                    .sharedElement(
                        state = rememberSharedContentState(key = "image/${pokemon.imageUrl}"),
                        animatedVisibilityScope = animatedVisibilityScope,
                        boundsTransform = { _, _ ->
                            tween(durationMillis = 1000)
                        }
                    )
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = pokemon.name
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "No. ${pokemon.number}",
                color = textColor,
                fontSize = 14.sp
            )
        }
    }
}
