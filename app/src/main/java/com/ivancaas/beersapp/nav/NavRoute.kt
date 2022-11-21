package com.ivancaas.beersapp.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

interface NavRoute<T : RouteNavigator> {

    val route: String
    val title: String

    @Composable
    fun Content(viewModel: T, navBackStack: NavBackStackEntry)

    @Composable
    fun viewModel(): T

    fun getArguments(): List<NamedNavArgument> = listOf()

    fun composable(
        builder: NavGraphBuilder,
        navHostController: NavHostController
    ) {
        builder.composable(route, getArguments()) { navBackStack ->
            val viewModel = viewModel()
            val viewStateAsState by viewModel.navigationState.collectAsState()

            LaunchedEffect(viewStateAsState) {

                updateNavigationState(navHostController, viewStateAsState, viewModel::onNavigated)
            }

            Content(viewModel, navBackStack)
        }
    }

    private fun updateNavigationState(
        navHostController: NavHostController,
        navigationState: NavigationState,
        onNavigated: (navState: NavigationState) -> Unit,
    ) {
        when (navigationState) {
            is NavigationState.NavigateToRoute -> {
                //IF I NEED TO BLOCK SOME BACK NAVIGATION, IF IT IS LINEAR I COULD BLOCK IT
                // ALSO WITH BACKHANDLER....
                /* if (navigationState.route == SomeRoute.route) {
                    navHostController.navigate(navigationState.route) {
                        popUpTo(SomeRoute.route) {
                            inclusive = false
                        }
                    }
                } else {
                    navHostController.navigate(navigationState.route)
*/
                navHostController.navigate(navigationState.route)
                onNavigated(navigationState)
            }
            is NavigationState.PopToRoute -> {
                navHostController.popBackStack(navigationState.staticRoute, false)
                onNavigated(navigationState)
            }
            is NavigationState.NavigateUp -> {
                navHostController.navigateUp()
                onNavigated(navigationState)

            }
            is NavigationState.PopBackStack -> {
                //TODO IF NEEDED
                onNavigated(navigationState)
            }
            is NavigationState.Idle -> {
            }
        }
    }
}

fun <T> SavedStateHandle.getOrThrow(key: String): T =
    get<T>(key) ?: throw IllegalArgumentException(
        "Mandatory argument $key missing in arguments."
    )