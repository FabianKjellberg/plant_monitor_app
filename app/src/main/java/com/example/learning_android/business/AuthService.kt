package com.example.learning_android.business

class AuthService {
    fun login(username: String, password: String): Boolean {
        return username == "fabian" && password == "1234"
    }
}