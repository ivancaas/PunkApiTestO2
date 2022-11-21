package com.ivancaas.beersapp.data.remote

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BeerRemoteDataSource {

    @GET("beers/{id}")
    fun getBeerById(
        @Path("id") id: String
    ): Single<BeersResponse>

    @GET("beers")
    fun getBeers(
        @Query("beer_name") beerName: String? = null,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): Single<BeersResponse> // ERROR TRYING TO USE RESULT BECAUSE IT EXPECT 1 OBJECT BUT ARRAY IS GIVEN


}