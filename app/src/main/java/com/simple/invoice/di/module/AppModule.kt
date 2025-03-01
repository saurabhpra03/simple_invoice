package com.simple.invoice.di.module

import android.content.Context
import com.simple.invoice.data.db.dao.AuthDao
import com.simple.invoice.data.db.dao.InvoiceDao
import com.simple.invoice.data.repository.AuthRepository
import com.simple.invoice.data.repository.AuthRepositoryImpl
import com.simple.invoice.data.repository.InvoiceRepository
import com.simple.invoice.data.repository.InvoiceRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun authRepository(
        @ApplicationContext context: Context,
        dao: AuthDao
    ): AuthRepository = AuthRepositoryImpl(context, dao)

    @Provides
    fun invoiceRepository(
        @ApplicationContext context: Context,
        dao: InvoiceDao
    ): InvoiceRepository = InvoiceRepositoryImpl(context, dao)


}