package me.harrydrummond.cafeapplication

import java.util.regex.Matcher
import java.util.regex.Pattern

object Validators {

    fun validateEmail(email: String): Boolean {
        val emailPattern = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        val pattern: Pattern = Pattern.compile(emailPattern)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }
}