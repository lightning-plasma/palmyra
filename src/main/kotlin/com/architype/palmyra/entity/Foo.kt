package com.architype.palmyra.entity

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import org.hibernate.validator.constraints.Range
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

@JsonPropertyOrder("foo", "bar", "fizz")
data class Foo(
    @get:JsonProperty("foo")
    @field:Min(1)
    @field:Max(5)
    val foo: String,

    @get:JsonProperty("bar")
    @field:NotBlank
    val bar: String,

    @get:JsonProperty("fizz")
    @field:Range(min = 1, max = 5)
    val fizz: String,
)
