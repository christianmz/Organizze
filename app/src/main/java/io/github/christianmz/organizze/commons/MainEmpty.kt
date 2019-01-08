package io.github.christianmz.organizze.commons

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import io.github.christianmz.organizze.activities.HomeActivity
import io.github.christianmz.organizze.activities.OnBoardingActivity
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

class MainEmpty : AppCompatActivity() {

    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (mAuth.currentUser == null) {
            startActivity(intentFor<OnBoardingActivity>().newTask().clearTask())
        } else {
            startActivity(intentFor<HomeActivity>().newTask().clearTask())
        }
        finish()
    }
}
