package com.architype.palmyra.runner

import com.architype.palmyra.entity.CsvSample
import com.fasterxml.jackson.dataformat.csv.CsvGenerator
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.fasterxml.jackson.module.kotlin.KotlinModule

fun main() {
    val csvMapper = CsvMapper().apply {
        registerModule(KotlinModule())
    }.configure(CsvGenerator.Feature.STRICT_CHECK_FOR_QUOTING, true)

    val csvSchema = csvMapper.schemaFor(CsvSample::class.java).withHeader()

    val foo = csvMapper.writer(csvSchema)
        .writeValueAsString(CsvSample("foo", "bar", "fizz"))
    println(foo)

    val bar = csvMapper.readerFor(CsvSample::class.java)
        .with(CsvSchema.emptySchema().withHeader())
        .readValues<CsvSample>(CSV)
        .readAll()
        .toList()
    println(bar)
}

fun createCsvSchema(): CsvSchema =
    CsvSchema.emptySchema()
        // .withHeader()
        .withLineSeparator("\n")
        .withColumnSeparator(',')

val CSV = """
大大カテゴリ,lCategoryCode,foo_bar_fizz
foo,bar,fizz
1,2,3
""".trimIndent()