package io.github.christianmz.organizze.commons

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText
import java.util.regex.Pattern

fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

fun isValidPassword(password: String): Boolean = Pattern.compile("(?=\\S+$).{6,}$").matcher(password).matches()

fun EditText.messageRealTime(message: (String) -> Unit){

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