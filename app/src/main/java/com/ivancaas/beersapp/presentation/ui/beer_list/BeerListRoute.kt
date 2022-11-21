package com.ivancaas.beersapp.presentation.ui.beer_list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.ivancaas.beersapp.nav.NavRoute

object BeerListRoute : NavRoute<BeerListViewModel> {

    //HERE WE COULD PASS ACTUALLY THE FULL BEER, BUT WE'RE GOING TO WORK WITH THE ID AND ANOTHER
    // NETWORK CALL
    override val route = "/beer_list"
    override val title = "Beer List"

    @Composable
    override fun Content(viewModel: BeerListViewModel, navBackStack: NavBackStackEntry) {
        val uiState by viewModel.uiState.collectAsState()

        BeerListScreen(viewModel, uiState)
    }

    @Composable
    override fun viewModel(): BeerListViewModel = hiltViewModel()
}