package com.example.expensetracker.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.expensetracker.data.model.ExpenseEntitity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expense_table")
    fun getAllExpenses(): Flow<List<ExpenseEntitity>>


    @Insert
    suspend fun insertExpense(expenseEntity: ExpenseEntitity)

    @Delete
    suspend fun deleteExpense(expenseEntity: ExpenseEntitity)

    @Update
    suspend fun updateExpense(expenseEntity: ExpenseEntitity)
}