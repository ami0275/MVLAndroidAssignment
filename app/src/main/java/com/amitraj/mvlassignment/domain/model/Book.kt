package com.amitraj.mvlassignment.domain.model

data class Book(
    val id: String,
    val locationA: Location,
    val locationB: Location,
    val price: Int
)
