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
import kotlinx.android.synthetic.main.activity_income.*

class IncomeActivity : AppCompatActivity() {

    private val value by lazy { et_incomes.text.toString() }
    private val date by lazy { et_date_incomes.text.toString() }
    private val category by lazy { et_category_incomes.text.toString() }
    private val description by lazy { et_description_incomes.text.toString() }

    private val databaseRef = mDatabaseRef.child(NODE_USERS).child(encryptedEmail())
    private var totalIncomes: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_income)

        recoverIncomes()
        et_date_incomes.setText(currentDate())

        fab_check_incomes.setOnClickListener {
            if (verifyFields(this, value, date, category, description)) {
                saveIncome()
            }
        }
    }

    private fun recoverIncomes() {

        databaseRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                totalIncomes = user?.incomesTotal
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    private fun saveIncome() {

        val transaction = Transaction(value.toDouble(), date, category, description, INCOME)

        totalIncomes?.let {
            val updateIncomes = value.toDouble() + it
            databaseRef.child(INCOME_TOTAL).setValue(updateIncomes)
        }

        transaction.saveTransaction(date)
        finish()
    }
}
