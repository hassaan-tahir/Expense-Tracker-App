package com.example.expensetracker.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expensetracker.R
import com.example.expensetracker.data.ExpenseDatabase
import com.example.expensetracker.data.dao.ExpenseDao
import com.example.expensetracker.data.model.ExpenseEntitity

class HomeViewModel(dao: ExpenseDao): ViewModel() {

    val expense = dao.getAllExpenses()

    fun getBalance(list: List<ExpenseEntitity>): String {

        var total = 0.0

        list.forEach{
            if (it.type == "Income"){
                total += it.amount
            }
            else {
                total = total - it.amount
            }
        }
        return "€ ${total}"
    }

    fun getTotalExpense(list: List<ExpenseEntitity>): String {

        var total = 0.0

        list.forEach{
            if (it.type == "Expense"){
                total += it.amount
            }
        }
        return "€ ${total}"

    }

    fun getTotalIncome(list: List<ExpenseEntitity>): String {

        var total = 0.0

        list.forEach{
            if (it.type == "Income"){
                total += it.amount
            }
        }
        return "€ ${total}"

    }

    fun getItemIcon(item:ExpenseEntitity): Int{
        if (item.category == "Paypal") {
            return R.drawable.ic_paypal
        }
        else if (item.category == "Netflix") {
            return R.drawable.ic_netflix
        } else if (item.category == "Starbucks") {
            return R.drawable.ic_starbucks
        }
        return  R.drawable.ic_paypal
    }
}

class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            val dao = ExpenseDatabase.getDatabase(context).expenseDao()
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}