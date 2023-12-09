package me.harrydrummond.cafeapplication.data.repository.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import me.harrydrummond.cafeapplication.data.model.Customer
import me.harrydrummond.cafeapplication.data.model.Employee
import me.harrydrummond.cafeapplication.data.repository.INotificationRepository
import me.harrydrummond.cafeapplication.data.repository.IOrderRepository
import me.harrydrummond.cafeapplication.data.repository.IPaymentRepository
import me.harrydrummond.cafeapplication.data.repository.IProductRepository
import me.harrydrummond.cafeapplication.data.repository.IReviewRepository
import me.harrydrummond.cafeapplication.data.repository.IUserRepository
import me.harrydrummond.cafeapplication.data.repository.contract.CustomerContract
import me.harrydrummond.cafeapplication.data.repository.contract.EmployeeContract
import me.harrydrummond.cafeapplication.data.repository.contract.NotificationContract
import me.harrydrummond.cafeapplication.data.repository.contract.OrderContract
import me.harrydrummond.cafeapplication.data.repository.contract.OrderDetailsContract
import me.harrydrummond.cafeapplication.data.repository.contract.PaymentContract
import me.harrydrummond.cafeapplication.data.repository.contract.ProductContract
import me.harrydrummond.cafeapplication.data.repository.contract.ReviewContract

private const val DataBaseName = "Cafe.db"
private const val ver : Int = 1

/**
 * Helper class for perofrming sqlite operations on the internal database.
 *
 * @param context Application context
 */
class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, DataBaseName, null, ver) {

    val customerRepository: IUserRepository<Customer> = SQLiteCustomerRepository(this)
    val productRepository: IProductRepository = SQLiteProductRepository(this)
    val employeeRepository: IUserRepository<Employee> = SQLiteEmployeeRepository(this)
    val orderRepository: IOrderRepository = SQLiteOrderRepository(this)
    val reviewRepository: IReviewRepository = SQLiteReviewRepository(this)
    val notificationRepository: INotificationRepository = SQLiteNotificationRepository(this)
    val paymentRepository: IPaymentRepository = SQLitePaymentRepository(this)

    override fun onCreate(db: SQLiteDatabase?) {
        val database = db ?: return

        try {
            // Create tables
            database.execSQL(ReviewContract.CREATE_TABLE)
            database.execSQL(PaymentContract.CREATE_TABLE)
            database.execSQL(EmployeeContract.CREATE_TABLE)
            database.execSQL(ProductContract.CREATE_TABLE)
            database.execSQL(CustomerContract.CREATE_TABLE)
            database.execSQL(OrderContract.CREATE_TABLE)
            database.execSQL(OrderDetailsContract.CREATE_TABLE)
            database.execSQL(NotificationContract.CREATE_TABLE)
        } catch (e: SQLiteException) {
            e.printStackTrace()
        }
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        // not required
    }
}