package com.architype.palmyra.entity

@JvmInline
value class Isbn(
    private val value: String
) {
    init {
        require(value.length == 13) {
            "isbnは13桁で管理する"
        }
    }

    fun rawValue() = value
}