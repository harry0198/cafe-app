package me.harrydrummond.cafeapplication.data.repository

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import me.harrydrummond.cafeapplication.data.model.UserModel

class UserRepository {
    companion object {
        const val DOCUMENT_NAME = "users"
    }

    private val db = Firebase.firestore

    fun saveUser(firebaseUser: FirebaseUser, userModel: UserModel): Task<Void> {
        val document = db.collection(DOCUMENT_NAME).document(firebaseUser.uid)
        return document.set(userModel)
    }

    fun registerAndSaveUser(email: String, password: String, userModel: UserModel): Task<Void> {
        return registerUserAuth(email, password).continueWithTask { task ->
            if (task.isSuccessful) {
                // Registration successful, now save user details
                val firebaseUser = task.result?.user
                if (firebaseUser != null) {
                    return@continueWithTask saveUser(firebaseUser, userModel)
                } else {
                    // Handle the case where the Firebase user is null
                    return@continueWithTask Tasks.forResult(null)
                }
            } else {
                // Handle the case where user registration failed
                return@continueWithTask Tasks.forException(task.exception!!)
            }
        }
    }

    private fun registerUserAuth(email: String, password: String): Task<AuthResult> {
        return Firebase.auth.createUserWithEmailAndPassword(email, password)
    }

    fun loginUser(email: String, password: String): Task<AuthResult> {
        return Firebase.auth.signInWithEmailAndPassword(email, password)
    }
    fun getUser(uid: String): Task<UserModel?> {
        val document = db.collection(DOCUMENT_NAME).document(uid)
        return document.get().continueWith { task: Task<DocumentSnapshot> ->
            if (task.isSuccessful) {
                val documentSnapshot = task.result
                documentSnapshot?.toObject(UserModel::class.java)
            } else {
                null
            }
        }
    }
}