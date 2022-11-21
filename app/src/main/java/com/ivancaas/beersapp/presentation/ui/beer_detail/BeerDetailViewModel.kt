package com.ivancaas.beersapp.presentation.ui.beer_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ivancaas.beersapp.App
import com.ivancaas.beersapp.R
import com.ivancaas.beersapp.data.remote.BeersResponse
import com.ivancaas.beersapp.data.repository.BeerRepository
import com.ivancaas.beersapp.nav.RouteNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.BufferedReader
import javax.inject.Inject

@HiltViewModel
class BeerDetailViewModel @Inject constructor(
    private val routerNavigator: RouteNavigator,
    private val beerRepository: BeerRepository
) : ViewModel(), RouteNavigator by routerNavigator {

    private val _uiState = MutableStateFlow<BeerDetailUiState>(BeerDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // initBriefing()
        }
        val myJson =
            App.instance.applicationContext.resources.openRawResource(R.raw.beersample)
        val gson = Gson()

        val fakeResponse1 = gson.fromJson(
            myJson.bufferedReader().use(BufferedReader::readText),
            BeersResponse::class.java
        )
    }

    suspend fun getBeerDetails(beerId: String) {
        try {
            val response = beerRepository.getBeerId(beerId).first()
            _uiState.update {
                BeerDetailUiState.Success(
                    response
                )
            }
        } catch (e: Exception) {
            _uiState.update {
                BeerDetailUiState.Failure(
                    e.message
                )
            }
            delay(1500)
            navigateUp()
        }

    }

}


interface BeerDetailUiState {
    object Loading : BeerDetailUiState
    data class Success(val beerDetail: BeersResponse.BeersResponseItem) : BeerDetailUiState
    data class Failure(val error: String?) : BeerDetailUiState
}