package com.architype.palmyra.entity

data class SqsMessage(
    val messageId: String,
    val body: String,
    val receiptHandle: String,
)