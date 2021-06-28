package com.architype.palmyra.entity

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("大大カテゴリ", "lCategoryCode", "foo_bar_fizz")
data class CsvSample(
    @get:JsonProperty("大大カテゴリ")
    val llCategoryCode: String,

    @get:JsonProperty("lCategoryCode")
    val lCategoryCode: String,

    @get:JsonProperty("foo_bar_fizz")
    val name: String
)