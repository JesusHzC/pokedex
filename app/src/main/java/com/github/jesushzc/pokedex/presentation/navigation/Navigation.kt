package com.github.jesushzc.pokedex.presentation.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.github.jesushzc.pokedex.presentation.ui.home.HomeScreen
import com.github.jesushzc.pokedex.presentation.ui.pokemon.PokemonScreen
import com.github.jesushzc.pokedex.utils.replaceWithSlash

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Navigation(
    navController: NavController = rememberNavController()
) {
    SharedTransitionLayout {
        NavHost(
            navController = navController as NavHostController,
            startDestination = Routes.HOME_SCREEN
        ) {
            composable(Routes.HOME_SCREEN) {
                HomeScreen(
                    animatedVisibilityScope = this,
                    onNavigateTo = { route ->
                        navController.navigate(route)
                    }
                )
            }

            composable(
                route = Routes.POKEMON_SCREEN + "/{name}/{image}/{number}/{color}",
                arguments = listOf(
                    navArgument("name") { type = NavType.StringType },
                    navArgument("image") { type = NavType.StringType },
                    navArgument("number") { type = NavType.IntType },
                    navArgument("color") { type = NavType.IntType }
                )
            ) {
                val name = it.arguments?.getString("name")!!
                val image = it.arguments?.getString("image")!!.replaceWithSlash()
                val number = it.arguments?.getInt("number")!!
                val color = it.arguments?.getInt("color")!!
                PokemonScreen(
                    pokemonName = name,
                    pokemonImage = image,
                    pokemonNumber = number,
                    color = color,
                    animatedVisibilityScope = this
                )
            }
        }
    }
}