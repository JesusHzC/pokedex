package com.github.jesushzc.pokedex.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.jesushzc.pokedex.R
import com.github.jesushzc.pokedex.presentation.navigation.Routes
import com.github.jesushzc.pokedex.presentation.theme.TopBarColor
import com.github.jesushzc.pokedex.utils.Constants.EMPTY_STRING

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomScaffold(
    title: String = EMPTY_STRING,
    showFloatingButton: Boolean = false,
    showToolbar: Boolean = true,
    onFloatingButtonClicked: () -> Unit = {},
    currentRoute: String = EMPTY_STRING,
    onNavigateTo: (String) -> Unit,
    content: @Composable () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (showToolbar) {
                CustomTopAppBar(
                    title = title,
                    scrollBehavior = scrollBehavior
                )
            }
        },
        bottomBar = {
            NavigationBottom(currentRoute, onNavigateTo)
        },
        floatingActionButton = {
            FloatingButton(
                showButton = showFloatingButton,
                onButtonClicked = onFloatingButtonClicked
            )
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            content()
        }
    }
}

private data class NavigationItem(
    val route: String,
    val iconEnabled: Painter,
    val iconDisabled: Painter,
    val title: String
)

@Composable
private fun NavigationBottom(
    currentRoute: String,
    onNavigateTo: (String) -> Unit
) {
    val items = listOf(
        NavigationItem(
            route = Routes.HOME_SCREEN,
            iconEnabled = painterResource(id = R.drawable.home_enabled),
            iconDisabled = painterResource(id = R.drawable.home_disabled),
            title = "Pokédex"
        ),

        NavigationItem(
            route = Routes.FAVORITES_SCREEN,
            iconEnabled = painterResource(id = R.drawable.favorites_enabled),
            iconDisabled = painterResource(id = R.drawable.favorites_disabled),
            title = "Favoritos"
        )
        /*NavigationItem(
            route = "",
            iconEnabled = painterResource(id = R.drawable.user_enabled),
            iconDisabled = painterResource(id = R.drawable.user_disabled),
            title = "Perfil"
        )*/
    )

    NavigationBar(
        containerColor = Color.White
    ) {
        items.forEachIndexed { index, navigationItem ->
            NavigationBarItem(
                icon = {
                    Image(
                        painter = if (currentRoute == navigationItem.route)
                            navigationItem.iconEnabled
                        else
                            navigationItem.iconDisabled,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.Crop
                    )
                },
                label = { Text(navigationItem.title) },
                selected = currentRoute == navigationItem.route,
                onClick = {
                    if (currentRoute != navigationItem.route)
                        onNavigateTo(navigationItem.route)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    title: String,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = TopBarColor,
            titleContentColor = Color.Black,
            scrolledContainerColor = TopBarColor,
        ),
        title = {
            Text(title, fontWeight = FontWeight.Bold)
        },
        scrollBehavior = scrollBehavior,
    )
}

@Composable
private fun FloatingButton(
    showButton: Boolean,
    onButtonClicked: () -> Unit
) {
    if (showButton) {
        ExtendedFloatingActionButton(
            onClick = { onButtonClicked() },
            icon = { Icon(Icons.Filled.KeyboardArrowUp, "Go to up") },
            text = { Text(text = "Ir al principio") },
        )
    }
}

@Preview
@Composable
private fun CustomScaffoldPreview() {
    CustomScaffold(
        title = "Pokédex",
        showFloatingButton = true,
        onFloatingButtonClicked = {},
        onNavigateTo = {}
    ){}
}