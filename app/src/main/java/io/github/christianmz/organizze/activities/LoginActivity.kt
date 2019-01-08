package io.github.christianmz.organizze.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import io.github.christianmz.organizze.R
import io.github.christianmz.organizze.commons.isValidEmail
import io.github.christianmz.organizze.commons.isValidPassword
import io.github.christianmz.organizze.commons.messageRealTime
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.*
import java.lang.Exception

class LoginActivity : AppCompatActivity() {

    private val mAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setSupportActionBar(toolbar_login)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btn_login.setOnClickListener { verifyCredentials() }
    }

    private fun loginUser(email: String, password: String) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {

            if (it.isSuccessful) {
                startActivity(intentFor<HomeActivity>().newTask().clearTask())
            } else {
                try {
                    throw it.exception!!
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    longToast(R.string.wrong_password)
                } catch (e: FirebaseAuthInvalidUserException) {
                    longToast(R.string.not_found_user)
                } catch (e: Exception) {
                    longToast("${R.string.error} ${e.message}")
                    e.printStackTrace()
                }
            }
        }
    }

    private fun verifyCredentials() {

        val email = et_email_login.text.toString()
        val password = et_password_login.text.toString()

        if (isValidEmail(email) && isValidPassword(password)) {
            loginUser(email, password)
        } else {
            when {
                email.isEmpty() -> longToast(R.string.please_add_email)
                password.isEmpty() -> longToast(R.string.please_add_password)
                !isValidEmail(email) -> longToast(R.string.invalid_email)
                !isValidPassword(password) -> longToast(R.string.weak_password)
                else -> longToast(R.string.check_your_info)
            }
        }

        et_email_login.messageRealTime { et_email_login.error = if (isValidEmail(it)) null else "Invalid E-mail" }
        et_password_login.messageRealTime { et_password_login.error = if (isValidPassword(it)) null else "Weak password, try a stronger one" }
    }
}
