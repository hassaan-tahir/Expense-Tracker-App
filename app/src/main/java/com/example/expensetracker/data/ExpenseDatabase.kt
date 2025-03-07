package com.example.expensetracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.expensetracker.data.dao.ExpenseDao
import com.example.expensetracker.data.model.ExpenseEntitity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [ExpenseEntitity::class], version = 1)
abstract class ExpenseDatabase: RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao

    companion object {
        const val DATABASE_NAME = "expense_database"

        @JvmStatic
        fun getDatabase(context: Context): ExpenseDatabase {
            return Room.databaseBuilder(
                context,
                ExpenseDatabase::class.java,
                DATABASE_NAME
            ).addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    InitBasicData(context)
                }

                fun InitBasicData(context: Context){
                    CoroutineScope(Dispatchers.IO).launch {
                        val dao = getDatabase(context).expenseDao()
                        dao.insertExpense(ExpenseEntitity(1, "Salary", 5000.0, System.currentTimeMillis(), "Salary", "Income"))
                        dao.insertExpense(ExpenseEntitity(2, "Food", 300.54, System.currentTimeMillis(), "Salary", "Expense"))
                        dao.insertExpense(ExpenseEntitity(3, "Dinner", 100.80, System.currentTimeMillis(), "Salary", "Expense"))
                        dao.insertExpense(ExpenseEntitity(4, "Paypal", 50021.0, System.currentTimeMillis(), "Salary", "Income"))

                    }
                }
            }).build()
        }
    }
}