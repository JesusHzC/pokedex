package com.github.jesushzc.pokedex.presentation.ui.favorites

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.github.jesushzc.pokedex.R
import com.github.jesushzc.pokedex.data.local.entity.PokemonEntity
import com.github.jesushzc.pokedex.data.local.entity.PokemonType
import com.github.jesushzc.pokedex.presentation.components.CustomScaffold
import com.github.jesushzc.pokedex.presentation.components.ErrorScreen
import com.github.jesushzc.pokedex.presentation.navigation.Routes
import com.github.jesushzc.pokedex.utils.Constants
import com.github.jesushzc.pokedex.utils.convertColor
import com.github.jesushzc.pokedex.utils.getContrastingTextColor
import com.github.jesushzc.pokedex.utils.parseTypeToColor
import com.github.jesushzc.pokedex.utils.parseTypeToImage

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = hiltViewModel(),
    onNavigateTo: (String) -> Unit
) {
    LaunchedEffect(key1 = true) {
        viewModel.getFavorites()
    }
    
    CustomScaffold(
        showFloatingButton = false,
        showToolbar = true,
        title = "Favorites",
        currentRoute = Routes.FAVORITES_SCREEN,
        onNavigateTo = onNavigateTo
    ) {
        FavoritesContent(
            viewModel = viewModel
        )
    }
}

@Composable
private fun FavoritesContent(viewModel: FavoritesViewModel) {
    when {
        viewModel.favorites.isEmpty() -> {
            ErrorScreen(
                image = R.drawable.empty_data,
                message = "No favorites yet"
            )
        }
        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = viewModel.favorites,
                    key = { pokemon -> pokemon._id }
                ) { pokemon ->
                    FavoriteCard(
                        pokemon = pokemon
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteCard(
    pokemon: PokemonEntity
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = convertColor(pokemon.details?.color!!),
            contentColor = getContrastingTextColor(pokemon.details?.color!!)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(8.dp)
            ) {
                Text(
                    text = "No. ${pokemon._id}",
                    color = getContrastingTextColor(pokemon.details?.color!!),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = pokemon.name.replaceFirstChar { it.uppercase() },
                    color = getContrastingTextColor(pokemon.details?.color!!),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    pokemon.types.forEach { type ->
                        CardType(type = type)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
            AsyncImage(
                model = pokemon.imageUrl,
                contentDescription = "Pokemon image",
                modifier = Modifier
                    .weight(1f),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun CardType(type: PokemonType) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(color = parseTypeToColor(type.name))
    ) {
        Row(
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(color = Color.White),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = parseTypeToImage(type.name)),
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = type.name.replaceFirstChar { it.uppercase() } ?: Constants.EMPTY_STRING,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}
