package me.harrydrummond.cafeapplication.data.repository

import org.mindrot.jbcrypt.BCrypt

/**
 * Utilities for password management.
 * Contains hashing and verification methods
 */
object PasswordUtils {

    /**
     * Hashes a password to bycrypt format.
     * @param password to hash
     * @return Hashed password
     */
    fun hashPassword(password: String): String {
        val salt = BCrypt.gensalt()
        return BCrypt.hashpw(password, salt)
    }

    /**
     * Verifies a plain password with a hashed password.
     * @param plainPassword Password to check
     * @param hashedPassword Password to check
     * @return If passwords are identical.
     */
    fun verifyPassword(plainPassword: String, hashedPassword: String): Boolean {
        return BCrypt.checkpw(plainPassword, hashedPassword)
    }
}