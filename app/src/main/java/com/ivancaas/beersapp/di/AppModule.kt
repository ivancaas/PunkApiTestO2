package com.ivancaas.beersapp.di

import com.ivancaas.beersapp.data.remote.BeerRemoteDataSource
import com.ivancaas.beersapp.data.repository.BeerRepository
import com.ivancaas.beersapp.nav.MyRouteNavigator
import com.ivancaas.beersapp.nav.RouteNavigator
import com.ivancaas.beersapp.util.AppDispatchers
import com.ivancaas.beersapp.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun bindRouteNavigator(): RouteNavigator = MyRouteNavigator()

    @Provides
    @Named("BaseUrl")
    fun provideBaseUrl() = BASE_URL.toHttpUrl()

    @Singleton
    @Provides
    fun provideInterceptor(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(@Named("BaseUrl") baseUrl: HttpUrl = provideBaseUrl()): Retrofit {
        return Retrofit.Builder().client(provideInterceptor())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl).build()
    }


    @Singleton
    @Provides
    fun provideCoroutinesDispatcher(): AppDispatchers = AppDispatchers()

    @Provides
    @Singleton
    fun provideBeerDataSource(): BeerRemoteDataSource = provideRetrofit()
        .create(BeerRemoteDataSource::class.java)


    @Provides
    @Singleton
    fun provideBeerRepository(
        beerRemoteDataSource: BeerRemoteDataSource,
        appDispatchers: AppDispatchers
        //   routeNavigator: RouteNavigator // If We need to navigate from the repository in case of err
    ): BeerRepository = BeerRepository(beerRemoteDataSource, appDispatchers)


}