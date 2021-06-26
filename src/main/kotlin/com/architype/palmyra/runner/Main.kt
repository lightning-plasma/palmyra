package com.architype.palmyra.runner

import com.architype.palmyra.mapper.CustomerMapper
import example.grpc.greeting.Customer
import ma.glasnost.orika.impl.DefaultMapperFactory
import org.mapstruct.factory.Mappers

fun main() {
    val customer = Customer.newBuilder()
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

    // orika
    // https://github.com/orika-mapper/orika
    // spring boot starter
    // https://github.com/akkinoc/orika-spring-boot-starter
    val mapperFactory = DefaultMapperFactory.Builder().build()
    val mapper = mapperFactory.mapperFacade

    val convertedByOrika = mapper.map(customer, com.architype.palmyra.entity.Customer::class.java)
    println(convertedByOrika)

    // mapStruct
    // https://github.com/mapstruct/mapstruct
    // https://mapstruct.org/
    // spring-bootのComponentに自動でMapperを設定することもできる
    val converter = Mappers.getMapper(CustomerMapper::class.java)
    val convertedByMapStruct = converter.mapping(customer)
    println(convertedByMapStruct)
}