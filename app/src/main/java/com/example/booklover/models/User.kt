package com.example.booklover.models

data class User(val id: String? = null, val firstName: String? = null, val lastName: String? = null, val email: String? = null, val password: String? = null, val phone: String? = null, val books: Map<String, Book>?= null)
