package io.github.christianmz.organizze.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import io.github.christianmz.organizze.R
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.startActivity

class HomeActivity : AppCompatActivity() {

    private val mAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        fab_incomes.setOnClickListener {
            mAuth.signOut()
            startActivity<SignUpActivity>()
        }
        fab_expenses.setOnClickListener {
            mAuth.signOut()
            startActivity<SignUpActivity>()
        }
    }
}
