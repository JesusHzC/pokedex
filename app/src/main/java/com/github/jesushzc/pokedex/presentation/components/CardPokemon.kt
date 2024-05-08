package com.github.jesushzc.pokedex.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.github.jesushzc.pokedex.R
import com.github.jesushzc.pokedex.domain.model.Pokemon
import com.github.jesushzc.pokedex.domain.model.PokemonEntry
import com.github.jesushzc.pokedex.domain.model.Type
import com.github.jesushzc.pokedex.domain.model.Types
import com.github.jesushzc.pokedex.utils.parseTypeToColor
import com.github.jesushzc.pokedex.utils.parseTypeToImage
import java.util.Locale

@Composable
fun CardPokemon(
    pokemon: PokemonEntry,
    modifier: Modifier = Modifier,
    cardColor: Color,
    textColor: Color
) {
    PokemonContent(
        pokemon = pokemon,
        modifier = modifier,
        cardColor = cardColor,
        textColor = textColor
    )
}

@Composable
private fun PokemonContent(
    pokemon: PokemonEntry,
    modifier: Modifier,
    cardColor: Color,
    textColor: Color
) {
    Card(
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight(),
        colors = CardDefaults.cardColors(
            containerColor = cardColor,
            contentColor = cardColor
        )
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

@Preview
@Composable
private fun CardPokemonPreview() {
    CardPokemon(
        pokemon = PokemonEntry(
            number = 1,
            name = "bulbasaur",
            imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
            details = Pokemon(
                id = 1,
                name = "bulbasaur",
                height = 7,
                weight = 69,
                types = listOf(
                    Types(
                        type = Type(
                            name = "grass",
                            url = "https://pokeapi.co/api/v2/type/12/"
                        )
                    ),
                )
            )
        ),
        cardColor = Color.White,
        textColor = Color.Black
    )
}