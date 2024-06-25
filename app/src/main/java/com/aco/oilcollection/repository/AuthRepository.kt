package com.aco.oilcollection.repository


import com.aco.oilcollection.database.User
import com.aco.oilcollection.database.UserDao

class AuthRepository(private val userDao: UserDao) {

    suspend fun registerUser(email: String, passwordHash: String, name: String): Result<User> {
        val existingUser = userDao.getUserByEmail(email)
        return if (existingUser != null) {
            Result.failure(Exception("User already exists"))
        } else {
            val newUser = User(email = email, passwordHash = passwordHash, name = name)
            userDao.insert(newUser)
            Result.success(newUser)
        }
    }

    suspend fun loginUser(email: String, passwordHash: String): Result<User> {
        val user = userDao.getUserByEmail(email)
        return if (user != null && user.passwordHash == passwordHash) {
            userDao.setLoginStatus(user.id, true)
            Result.success(user)
        } else {
            Result.failure(Exception("Invalid email or password"))
        }
    }
}
