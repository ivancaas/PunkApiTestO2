package com.ivancaas.beersapp.presentation.ui.beer_list

import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivancaas.beersapp.App
import com.ivancaas.beersapp.data.remote.BeersResponse
import com.ivancaas.beersapp.data.repository.BeerRepository
import com.ivancaas.beersapp.nav.RouteNavigator
import com.ivancaas.beersapp.presentation.ui.beer_detail.BeerDetailRoute
import com.ivancaas.beersapp.util.then
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeerListViewModel @Inject constructor(
    private val routerNavigator: RouteNavigator,
    private val beerRepository: BeerRepository
) : ViewModel(), RouteNavigator by routerNavigator {

    private val _uiState = MutableStateFlow<BeerListUiState>(BeerListUiState.Loading)
    val uiState = _uiState.asStateFlow()
    val currentPage = mutableStateOf(1)
    val noMorePages = mutableStateOf(false)
    val beerLoading = mutableStateOf(false)

    init {
        _uiState.update {
            BeerListUiState.Loading
        }
        viewModelScope.launch {
            getBeerList()
        }
    }

    suspend fun getBeerList(beerName: String = "", isNextPage: Boolean = false) {
        if (!noMorePages.value) {
            viewModelScope.launch {
                try {
                    beerLoading.value = true
                    val result = beerRepository.getBeers(
                        beerName,
                        isNextPage.then(++currentPage.value) ?: currentPage.value
                    )
                    if (result != null && result.isNotEmpty() && currentPage.value != 1) {
                        if (_uiState.value is BeerListUiState.Success) {
                            _uiState.update {
                                result.addAll((it as BeerListUiState.Success).beerList)
                                beerLoading.value = false
                                BeerListUiState.Success(
                                    result
                                )
                            }
                        } else {
                            _uiState.update {
                                beerLoading.value = false
                                BeerListUiState.Success(
                                    result
                                )
                            }
                        }
                    } else {
                        beerLoading.value = false
                        noMorePages.value = true
                        Toast.makeText(App.instance, "No hay mas cervezas ;)", Toast.LENGTH_SHORT)
                            .show()
                    }
                } catch (e: Exception) {
                    beerLoading.value = false

                    _uiState.update {
                        BeerListUiState.Failure(
                            e.message + "\n intentalo de  nuevo mas tarde"
                        )
                    }
                }
            }
        }
        // I could put some message in the bottom instead of this(or nothing)
        else Toast.makeText(App.instance, "No hay mas cervezas ;)", Toast.LENGTH_SHORT).show()
    }

    fun navigateToDetail(beerId: String) {
        navigateToRoute(BeerDetailRoute.route.replace("{id}", beerId))
    }

}


interface BeerListUiState {
    object Loading : BeerListUiState
    data class Success(val beerList: ArrayList<BeersResponse.BeersResponseItem>) : BeerListUiState
    data class Failure(val error: String?) : BeerListUiState
}