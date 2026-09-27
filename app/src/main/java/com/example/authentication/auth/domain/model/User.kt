package com.example.authentication.auth.domain.model

data class User(
    val id : Int,
    val phoneNumber : String,
    val isVerified : Boolean
)
