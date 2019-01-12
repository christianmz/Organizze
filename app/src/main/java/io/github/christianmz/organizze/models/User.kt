package io.github.christianmz.organizze.models

import com.google.firebase.database.Exclude
import io.github.christianmz.organizze.commons.NODE_USERS
import io.github.christianmz.organizze.commons.mDatabaseRef

data class User(
    @get:Exclude var id: String = "",
    var name: String = "",
    var email: String = "",
    @get:Exclude var password: String = "",
    var incomesTotal: Double = 0.0,
    var expensesTotal: Double = 0.0
) {

    fun saveUser() {
        mDatabaseRef
            .child(NODE_USERS)
            .child(this.id)
            .setValue(this)
    }
}