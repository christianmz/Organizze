package io.github.christianmz.organizze.models

import io.github.christianmz.organizze.commons.*

data class Transaction(
    val value: Double = 0.0,
    val date: String = "",
    val category: String = "",
    val description: String = "",
    val type: String = "",
    var key: String? = null
) {

    fun saveTransaction(chosenDate: String) {
        val date = monthYearChosen(chosenDate)
        mDatabaseRef.child(NODE_TRANSACTIONS)
            .child(encryptedEmail())
            .child(date)
            .push()
            .setValue(this)
    }
}