package com.audiospotapp.utils

class EmailUtils {

    companion object{
        fun isValidEmail(email: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }
}