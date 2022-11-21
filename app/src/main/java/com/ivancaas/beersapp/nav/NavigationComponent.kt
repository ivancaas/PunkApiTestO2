package com.ivancaas.beersapp.nav

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ivancaas.beersapp.presentation.ui.beer_detail.BeerDetailRoute
import com.ivancaas.beersapp.presentation.ui.beer_list.BeerListRoute


@Composable
fun NavigationComponent(navHostController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navHostController,
        startDestination = BeerListRoute.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        BeerListRoute.composable(this, navHostController)
        BeerDetailRoute.composable(this, navHostController)
    }
}
