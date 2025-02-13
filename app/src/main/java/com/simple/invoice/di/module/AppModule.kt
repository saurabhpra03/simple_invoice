package com.simple.invoice.di.module

import android.content.Context
import com.simple.invoice.data.db.dao.AuthDao
import com.simple.invoice.di.qualifer.QualifierMyAppDB
import com.simple.invoice.base.MyAppDB
import com.simple.invoice.data.db.dao.InvoiceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @QualifierMyAppDB
    @Singleton
    @Provides
    fun provideDB(@ApplicationContext context: Context) : MyAppDB = MyAppDB.getDatabase(context)

    @Singleton
    @Provides
    fun provideAuthDao(@QualifierMyAppDB db: MyAppDB) : AuthDao = db.authDao()

    @Singleton
    @Provides
    fun provideInvoiceDao(@QualifierMyAppDB db: MyAppDB): InvoiceDao = db.invoiceDao()

}