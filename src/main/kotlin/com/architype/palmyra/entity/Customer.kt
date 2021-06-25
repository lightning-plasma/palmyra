package com.architype.palmyra.entity

data class Customer(
    val firstName: String,
    val lastName: String,
    val nickName: String,
    val age: Int,
    val address: String,
    val tel: String,
    val emailAddress: String,
    val hobby: String,
    val hasCar: Boolean,
    val extra: String
)
