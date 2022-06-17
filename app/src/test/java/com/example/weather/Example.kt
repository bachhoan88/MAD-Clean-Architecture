package com.example.weather

class Example {
    fun test() {
        val user = User(1, "Example")
        val otherUser = user.copy(id = 2)
    }
}

data class User(val id: Int, val name: String)