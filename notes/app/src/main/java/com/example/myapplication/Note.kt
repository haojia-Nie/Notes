package com.example.myapplication


// simple data class for a single note
data class Note(
    val id: Long?,
    var title: String = "",
    var body: String = "",
    var important: Boolean = false,
    var isSelected: Boolean = false
)