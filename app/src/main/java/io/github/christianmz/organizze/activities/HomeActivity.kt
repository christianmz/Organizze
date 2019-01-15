package io.github.christianmz.organizze.activities

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import io.github.christianmz.organizze.R
import io.github.christianmz.organizze.adapters.TransactionAdapter
import io.github.christianmz.organizze.commons.*
import io.github.christianmz.organizze.models.Transaction
import io.github.christianmz.organizze.models.User
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.startActivity

class HomeActivity : AppCompatActivity() {

    private var incomes: Double? = null
    private var expenses: Double? = null
    private var balance: Double? = null

    private lateinit var valueEventListenerUser: ValueEventListener
    private lateinit var valueEventListenerTransaction: ValueEventListener
    private lateinit var monthYear: String

    private lateinit var transaction: Transaction
    private val transactions = ArrayList<Transaction>()
    private val dbRefUser = mDatabaseRef.child(NODE_USERS).child(encryptedEmail())
    private val dbRefTransaction = mDatabaseRef.child(NODE_TRANSACTIONS).child(encryptedEmail())

    private val adapter by lazy { TransactionAdapter(transactions) }
    private val recycler: RecyclerView by lazy { rv_month_transactions }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        setCalendar()
        setRecycler()
        setSwipe()

        fab_incomes.setOnClickListener { startActivity<IncomeActivity>() }
        fab_expenses.setOnClickListener { startActivity<ExpenseActivity>() }
    }

    override fun onStart() {
        super.onStart()
        setBalanceUser()
        setTransactions()
    }

    override fun onStop() {
        super.onStop()
        dbRefUser.removeEventListener(valueEventListenerUser)
        dbRefTransaction.removeEventListener(valueEventListenerTransaction)
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

    private fun setCalendar() {

        val currentDate = calendar_view.currentDate
        val months = arrayOf<CharSequence>(
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio",
            "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        )

        monthYear = "${String.format("%02d", currentDate.month + 1)}${currentDate.year}"
        calendar_view.setTitleMonths(months)

        calendar_view.setOnMonthChangedListener { _, date ->
            monthYear = "${String.format("%02d", date.month + 1)}${date.year}"
            dbRefTransaction.removeEventListener(valueEventListenerTransaction)
            setTransactions()
        }
    }

    private fun setRecycler() {
        recycler.setHasFixedSize(true)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    private fun setBalanceUser() {

        valueEventListenerUser = dbRefUser.addValueEventListener(object : ValueEventListener {

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

    private fun setTransactions() {

        valueEventListenerTransaction = dbRefTransaction.child(monthYear)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    transactions.clear()
                    for (data in dataSnapshot.children) {
                        val transaction = data.getValue(Transaction::class.java)
                        transaction?.key = data.key
                        transaction?.let { transactions.add(it) }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
    }

    private fun setSwipe() {

        val itemTouch = object : ItemTouchHelper.Callback() {

            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
                val dragFlags = ItemTouchHelper.ACTION_STATE_IDLE
                val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) = deleteTransaction(viewHolder)
        }
        ItemTouchHelper(itemTouch).attachToRecyclerView(recycler)
    }

    private fun deleteTransaction(viewHolder: RecyclerView.ViewHolder) {

        AlertDialog
            .Builder(this)
            .setTitle(R.string.delete_transaction)
            .setCancelable(false)
            .setPositiveButton(R.string.yes) { _, _ ->
                val position = viewHolder.adapterPosition
                transaction = transactions[position]
                transaction.key?.let { dbRefTransaction.child(monthYear).child(it).removeValue() }
                adapter.notifyItemRemoved(position)
                updateBalance()
            }.setNegativeButton(R.string.cancel) { _, _ ->
                adapter.notifyDataSetChanged()
            }.create()
            .show()
    }

    private fun updateBalance() {
        when (transaction.type) {
            INCOME -> {
                incomes = incomes?.minus(transaction.value)
                dbRefUser.child(INCOME_TOTAL).setValue(incomes)
            }
            EXPENSE -> {
                expenses = expenses?.minus(transaction.value)
                dbRefUser.child(EXPENSE_TOTAL).setValue(expenses)
            }
        }
    }
}
