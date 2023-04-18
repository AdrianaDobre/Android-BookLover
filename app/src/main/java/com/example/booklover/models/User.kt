package com.example.booklover.models

data class User(val id: String, val firstName: String, val lastName: String, val email: String, val password: String, val phone: String){
    constructor() : this("","","","","","")
}
