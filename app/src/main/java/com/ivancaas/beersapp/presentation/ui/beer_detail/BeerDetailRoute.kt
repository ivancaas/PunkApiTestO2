package com.ivancaas.beersapp.presentation.ui.beer_detail

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavBackStackEntry
import com.ivancaas.beersapp.App
import com.ivancaas.beersapp.nav.NavRoute
import kotlinx.coroutines.launch

object BeerDetailRoute : NavRoute<BeerDetailViewModel> {

    //HERE WE COULD PASS ACTUALLY THE FULL BEER, BUT WE'RE GOING TO WORK WITH THE ID AND ANOTHER
    // NETWORK CALL
    override val route = "/beer_details/{id}"
    override val title = "Beer Details"

    @Composable
    override fun Content(viewModel: BeerDetailViewModel, navBackStack: NavBackStackEntry) {
        val uiState by viewModel.uiState.collectAsState()
        val beerId = navBackStack.arguments?.getString("id")
        LaunchedEffect(key1 = beerId) {
            viewModel.viewModelScope.launch {
                if (beerId != null)
                    viewModel.getBeerDetails(beerId)
                else {
                    Toast.makeText(App.instance, "Error getting the beer", Toast.LENGTH_SHORT)
                        .show()
                    viewModel.navigateUp()
                }

            }
        }

        BeerDetailScreen(viewModel, uiState)
    }

    @Composable
    override fun viewModel(): BeerDetailViewModel = hiltViewModel()
}