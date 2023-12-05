package me.harrydrummond.cafeapplication.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.harrydrummond.cafeapplication.data.model.Customer
import me.harrydrummond.cafeapplication.data.model.Employee
import me.harrydrummond.cafeapplication.data.repository.IOrderRepository
import me.harrydrummond.cafeapplication.data.repository.sqlite.DataBaseHelper
import me.harrydrummond.cafeapplication.data.repository.IProductRepository
import me.harrydrummond.cafeapplication.data.repository.IReviewRepository
import me.harrydrummond.cafeapplication.data.repository.IUserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {

    @Provides
    @Singleton
    fun provideCustomerRepository(database: DataBaseHelper): IUserRepository<Customer> {
        return database.customerRepository
    }

    @Provides
    @Singleton
    fun provideEmployeeRepository(database: DataBaseHelper): IUserRepository<Employee> {
        return database.employeeRepository
    }

    @Provides
    @Singleton
    fun provideProductRepository(database: DataBaseHelper): IProductRepository {
        return database.productRepository
    }

    @Provides
    @Singleton
    fun provideOrderRepository(database: DataBaseHelper): IOrderRepository {
        return database.orderRepository
    }

    @Provides
    @Singleton
    fun provideReviewRepository(database: DataBaseHelper): IReviewRepository {
        return database.reviewRepository
    }

    @Provides
    @Singleton
    fun provideDataBaseHelper(@ApplicationContext context: Context): DataBaseHelper {
        val dbHelper = DataBaseHelper(context)
        dbHelper.writableDatabase
        dbHelper.close()
        return dbHelper
    }
}