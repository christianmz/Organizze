package io.github.christianmz.organizze.models

import com.google.firebase.database.Exclude
import com.google.firebase.database.FirebaseDatabase

data class User(
    @get:Exclude var id: String = "",
    var name: String = "",
    var email: String = "",
    @get:Exclude var password: String = ""
) {

    companion object {
        private const val FIRST_NODE = "users"
    }

    private val firebaseDatabase = FirebaseDatabase.getInstance().reference

    fun saveUser() {
        firebaseDatabase.child(FIRST_NODE).child(this.id).setValue(this)
    }
}