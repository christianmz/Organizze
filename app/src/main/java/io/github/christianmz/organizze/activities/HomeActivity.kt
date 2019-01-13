package io.github.christianmz.organizze.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import io.github.christianmz.organizze.R
import io.github.christianmz.organizze.commons.NODE_USERS
import io.github.christianmz.organizze.commons.encryptedEmail
import io.github.christianmz.organizze.commons.mAuth
import io.github.christianmz.organizze.commons.mDatabaseRef
import io.github.christianmz.organizze.models.User
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.startActivity

class HomeActivity : AppCompatActivity() {

    private var incomes: Double? = null
    private var expenses: Double? = null
    private var balance: Double? = null

    private val databaseRef = mDatabaseRef.child(NODE_USERS).child(encryptedEmail())
    private lateinit var valueEventListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        configCalendar()
        fab_incomes.setOnClickListener { startActivity<IncomeActivity>() }
        fab_expenses.setOnClickListener { startActivity<ExpenseActivity>() }
    }

    override fun onStart() {
        super.onStart()
        recoverBalance()
    }

    override fun onStop() {
        super.onStop()
        databaseRef.removeEventListener(valueEventListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
           R.id.item_exit -> {
               mAuth.signOut()
               startActivity<OnBoardingActivity>()
               finish()
           }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun configCalendar() {

        val months = arrayOf<CharSequence>(
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio",
            "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        )
        calendar_view.setTitleMonths(months)
        calendar_view.setOnMonthChangedListener { widget, date ->

        }
    }

    private fun recoverBalance() {

        valueEventListener = databaseRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val user = dataSnapshot.getValue(User::class.java)
                incomes = user?.incomesTotal
                expenses = user?.expensesTotal
                balance = expenses?.let { incomes?.minus(it) }

                tv_greeting.text = "Hello, ${user?.name}"
                tv_balance.text = "$ ${balance?.toInt()}"
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }
}
