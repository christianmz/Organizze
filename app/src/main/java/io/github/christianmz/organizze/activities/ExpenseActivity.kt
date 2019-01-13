package io.github.christianmz.organizze.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import io.github.christianmz.organizze.R
import io.github.christianmz.organizze.commons.*
import io.github.christianmz.organizze.models.Transaction
import io.github.christianmz.organizze.models.User
import kotlinx.android.synthetic.main.activity_expense.*

class ExpenseActivity : AppCompatActivity() {

    private val value by lazy { et_expense.text.toString() }
    private val date by lazy { et_date_expense.text.toString() }
    private val category by lazy { et_category_expense.text.toString() }
    private val description by lazy { et_description_expense.text.toString() }

    private val databaseRef = mDatabaseRef.child(NODE_USERS).child(encryptedEmail())
    private var totalExpense: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense)

        recoverExpenses()
        et_date_expense.setText(currentDate())

        fab_check_expense.setOnClickListener {
            if (verifyFields(this, value, date, category, description)) {
                saveExpense()
            }
        }
    }

    private fun recoverExpenses() {

        databaseRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                totalExpense = user?.expensesTotal
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    private fun saveExpense() {

        val transaction = Transaction(value.toDouble(), date, category, description, EXPENSE)

        totalExpense?.let {
            val updateExpenses = value.toDouble() + it
            databaseRef.child(EXPENSE_TOTAL).setValue(updateExpenses)
        }

        transaction.saveTransaction(date)
        finish()
    }
}
