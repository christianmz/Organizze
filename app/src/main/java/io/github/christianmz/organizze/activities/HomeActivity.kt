package io.github.christianmz.organizze.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.github.christianmz.organizze.R
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.startActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        fab_incomes.setOnClickListener { startActivity<IncomeActivity>() }
        fab_expenses.setOnClickListener { startActivity<ExpenseActivity>() }
    }
}
