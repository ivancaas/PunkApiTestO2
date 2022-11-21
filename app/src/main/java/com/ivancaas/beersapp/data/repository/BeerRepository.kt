package com.ivancaas.beersapp.data.repository

import com.ivancaas.beersapp.data.remote.BeerRemoteDataSource
import com.ivancaas.beersapp.util.AppDispatchers
import com.ivancaas.beersapp.util.then
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BeerRepository @Inject constructor(
    private val beerRemoteDataSource: BeerRemoteDataSource,
    private val appDispatchers: AppDispatchers
) {

    suspend fun getBeers(beerName: String, page: Int, perPage: Int = 50) =
        withContext(appDispatchers.io) {
            /*
            * With "then"(wrapped if) we check if beerName is empty, and if it is we return null to
            * make retrofit avoid sending beerName param empty
            */
            val result = beerRemoteDataSource.getBeers(
                beerName.isNotEmpty().then(beerName),
                page,
                perPage
            )
            return@withContext result.blockingGet()
        }

    suspend fun getBeerId(beerId: String) = withContext(appDispatchers.io) {

        val result = beerRemoteDataSource.getBeerById(beerId)
        return@withContext result.blockingGet()

    }

}