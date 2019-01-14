package io.github.christianmz.organizze.commons

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import io.github.christianmz.organizze.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


/** Unique Instances **/

val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
val mDatabaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference


/** Constant **/

const val NODE_USERS = "users"
const val NODE_TRANSACTIONS = "transactions"
const val INCOME = "income"
const val EXPENSE = "expense"
const val INCOME_TOTAL = "incomesTotal"
const val EXPENSE_TOTAL = "expensesTotal"


/** Functions **/

fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

fun isValidPassword(password: String): Boolean = Pattern.compile("(?=\\S+$).{6,}$").matcher(password).matches()

fun ViewGroup.inflate(layoutID: Int): View = LayoutInflater.from(context).inflate(layoutID, this, false)

fun encodeBase64(text: String) = Base64.encodeToString(text.toByteArray(), Base64.DEFAULT).replace("(\\n|\\r)".toRegex(), "")

fun decodeBase64(textCoded: String) = String(Base64.decode(textCoded, Base64.DEFAULT))

fun currentDate(): String = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT).format(System.currentTimeMillis())

fun monthYearChosen(date: String): String {
    val dateChosen = date.split("/")
    return "${dateChosen[1]}${dateChosen[2]}"
}

fun encryptedEmail(): String {
    val emailUser = mAuth.currentUser?.email
    return Base64.encodeToString(emailUser?.toByteArray(), Base64.DEFAULT).replace("(\\n|\\r)".toRegex(), "")
}

fun verifyFields(context: Context, value: String, date: String, category: String, description: String): Boolean {

    return if (value.isNotEmpty() && date.isNotEmpty() && category.isNotEmpty() && description.isNotEmpty()) {
        true
    } else {
        when {
            value.isEmpty() -> Toast.makeText(context, R.string.add_value, Toast.LENGTH_LONG).show()
            date.isEmpty() -> Toast.makeText(context, R.string.add_date, Toast.LENGTH_LONG).show()
            category.isEmpty() -> Toast.makeText(context, R.string.add_category, Toast.LENGTH_LONG).show()
            description.isEmpty() -> Toast.makeText(context, R.string.add_description, Toast.LENGTH_LONG).show()
            else -> Toast.makeText(context, R.string.check_your_info, Toast.LENGTH_LONG).show()
        }
        false
    }
}


/** Extension Functions **/

fun EditText.messageRealTime(message: (String) -> Unit) {

    this.addTextChangedListener(object : TextWatcher {

        override fun afterTextChanged(editable: Editable) {
            message(editable.toString())
        }

        override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
        }
    })
}
