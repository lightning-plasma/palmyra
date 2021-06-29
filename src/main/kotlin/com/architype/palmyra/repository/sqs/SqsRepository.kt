package com.architype.palmyra.repository.sqs

import kotlinx.coroutines.CoroutineScope
import org.springframework.stereotype.Repository
import software.amazon.awssdk.services.sqs.SqsAsyncClient

@Repository
class SqsRepository(
    private val sqsAsyncClient: SqsAsyncClient
) {
    // read
    fun CoroutineScope.receive() {

    }

    // send batch
    fun send() {

    }

    // delete batch
    fun delete() {

    }
}