package com.architype.palmyra.mapper

import example.grpc.greeting.Customer
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper
interface CustomerMapper {
    fun mapping(customer: Customer): com.architype.palmyra.entity.Customer

    companion object {
        val INSTANCE: CustomerMapper = Mappers.getMapper(CustomerMapper::class.java)
    }
}