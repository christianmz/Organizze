package io.github.christianmz.organizze.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.FirebaseDatabase
import io.github.christianmz.organizze.R
import io.github.christianmz.organizze.commons.isValidEmail
import io.github.christianmz.organizze.commons.isValidPassword
import io.github.christianmz.organizze.commons.messageRealTime
import io.github.christianmz.organizze.helpers.Base64Custom
import io.github.christianmz.organizze.models.User
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.jetbrains.anko.*
import java.lang.Exception

class SignUpActivity : AppCompatActivity() {

    private val mAuth by lazy { FirebaseAuth.getInstance() }
    private val name by lazy { et_name_sign_up.text.toString() }
    private val email by lazy { et_email_sign_up.text.toString() }
    private val password by lazy { et_password_sign_up.text.toString() }
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        setSupportActionBar(toolbar_register)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btn_sign_up.setOnClickListener { verifyCredentials() }
    }

    private fun createUser(email: String, password: String) {

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { it ->
                if (it.isSuccessful) {

                    val id = Base64Custom.encode(user.email)
                    user.id = id
                    user.saveUser()

                    startActivity(intentFor<HomeActivity>().newTask().clearTask())

                } else {

                    try {
                        throw it.exception!!
                    } catch (e: FirebaseAuthUserCollisionException) {
                        longToast(R.string.email_already_exists)
                    } catch (e: Exception) {
                        longToast("${R.string.error} ${e.message}")
                        e.printStackTrace()
                    }
                }
            }
    }

    private fun verifyCredentials() {

        if (isValidEmail(email) && isValidPassword(password)) {

            user = User(name = name, email = email, password = password)
            createUser(email, password)

        } else {
            when {
                name.isEmpty() -> longToast(R.string.please_add_name)
                email.isEmpty() -> longToast(R.string.please_add_email)
                password.isEmpty() -> longToast(R.string.please_add_password)
                !isValidEmail(email) -> longToast(R.string.invalid_email)
                !isValidPassword(password) -> longToast(R.string.weak_password)
                else -> longToast(R.string.check_your_info)
            }
        }

        et_email_sign_up.messageRealTime {
            et_email_sign_up.error = if (isValidEmail(it)) null else "Invalid E-mail"
        }
        et_password_sign_up.messageRealTime {
            et_password_sign_up.error = if (isValidPassword(it)) null else "Weak password, try a stronger one"
        }
    }
}
