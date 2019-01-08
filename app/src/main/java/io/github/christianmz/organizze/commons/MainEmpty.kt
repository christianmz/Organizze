package io.github.christianmz.organizze.commons

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.github.christianmz.organizze.activities.HomeActivity
import io.github.christianmz.organizze.activities.OnBoardingActivity
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.newTask

class MainEmpty : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ConfigFirebase.authInstance.currentUser == null) {
            startActivity(intentFor<OnBoardingActivity>().newTask().clearTask())
        } else {
            startActivity(intentFor<HomeActivity>().newTask().clearTask())
        }
        finish()
    }
}
