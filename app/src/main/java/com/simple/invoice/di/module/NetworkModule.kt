package com.simple.invoice.di.module

import com.simple.invoice.data.networking.CoroutineDispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun providesCoroutine(): CoroutineDispatcherProvider = CoroutineDispatcherProvider()

}