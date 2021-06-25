package com.architype.palmyra.runner

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.protobuf.util.JsonFormat
import example.grpc.greeting.Customer
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import java.util.concurrent.TimeUnit

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
open class PalmyraBenchMaker {
    private val customer = Customer.newBuilder()
        .setFirstName("foo")
        .setLastName("bar")
        .setNickName("fizz")
        .setAge(20)
        .setAddress("some place")
        .setTel("00011112222")
        .setEmailAddress("foo@bar.fizz")
        .setHobby("none")
        .setHasCar(true)
        .setExtra("nothing")
        .build()

    private val objectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    fun convertByJackson() {
        val jsonString = JsonFormat.printer().print(customer)
        val converted = objectMapper.readValue(jsonString, com.architype.palmyra.entity.Customer::class.java)
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    fun convertByOwn() {
        val converted = com.architype.palmyra.entity.Customer(
            firstName = customer.firstName,
            lastName = customer.lastName,
            nickName = customer.nickName,
            age = customer.age,
            address = customer.address,
            tel = customer.tel,
            emailAddress = customer.emailAddress,
            hobby = customer.hobby,
            hasCar = customer.hasCar,
            extra = customer.extra
        )
    }
}