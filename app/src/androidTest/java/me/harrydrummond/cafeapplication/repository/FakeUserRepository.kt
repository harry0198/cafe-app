package me.harrydrummond.cafeapplication.repository

import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import me.harrydrummond.cafeapplication.data.repository.IUserRepository
//
//class FakeUserRepository: IUserRepository {
//
//    private val userCredentials = mutableMapOf<String, String>()
//    private val users = mutableMapOf<String, UserModel>()
//    private val userCart = mutableMapOf<String, Cart>()
//    private var loggedInUserId: String? = null
//
//    /**
//     * Saves user to the fake database and returns task
//     */
//    override fun saveUser(userId: String, userModel: UserModel): Task<Any> {
//        return Tasks.forResult(users.put(userId, userModel))
//    }
//
//    override fun registerUser(email: String, password: String): Task<Any> {
//        val tcs = TaskCompletionSource<Any>()
//        tcs.setResult {
//            if (userCredentials.containsKey(email)) {
//                throw FirebaseAuthUserCollisionException("", "")
//            }
//
//            userCredentials[email] = password
//        }
//
//        return tcs.task
//    }
//
//    override fun loginUser(email: String, password: String): Task<Any> {
//        val tcs = TaskCompletionSource<Any>()
//        tcs.setResult {
//            if (!userCredentials.containsKey(email)) {
//                throw FirebaseAuthInvalidUserException("", "")
//            } else if (userCredentials[email] != password) {
//                throw FirebaseAuthInvalidCredentialsException("", "")
//            }
//
//            userCredentials[email] = password
//            loggedInUserId = email
//        }
//
//        Tasks.await(tcs.task)
//
//        return tcs.task
//    }
//
//    override fun getLoggedInUserId(): String? {
//        return loggedInUserId
//    }
//
//    override fun logoutUser() {
//        loggedInUserId = null
//    }
//
//    override fun getUser(uid: String): Task<UserModel?> {
//        return Tasks.forResult(users[uid])
//    }
//
//    override fun saveUserCart(cart: Cart): Task<Boolean> {
//        userCart[getLoggedInUserId()!!] = cart
//        return Tasks.forResult(true)
//    }
//
//    override fun partialLoadUserCart(): Task<Cart?> {
//        return Tasks.forResult(userCart[getLoggedInUserId()])
//    }
//}