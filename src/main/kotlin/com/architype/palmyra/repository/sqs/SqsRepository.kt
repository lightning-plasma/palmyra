package com.architype.palmyra.repository.sqs

import com.architype.palmyra.entity.SqsMessage
import com.architype.palmyra.runner.await
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import org.springframework.stereotype.Repository
import software.amazon.awssdk.services.sqs.SqsAsyncClient
import software.amazon.awssdk.services.sqs.model.DeleteMessageBatchRequest
import software.amazon.awssdk.services.sqs.model.DeleteMessageBatchRequestEntry
import software.amazon.awssdk.services.sqs.model.DeleteMessageBatchResponse
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest
import java.util.concurrent.CompletableFuture

@Repository
class SqsRepository(
    private val sqsAsyncClient: SqsAsyncClient
) {
    suspend fun getUrl(queueName: String): CompletableFuture<GetQueueUrlResponse> {
        val request = GetQueueUrlRequest.builder()
            .queueName(queueName)
            .build()

        return sqsAsyncClient.getQueueUrl(request)
    }

    suspend fun receive(url: String, channel: SendChannel<SqsMessage>) {
        // 安全装置として無限Loopはやめておく...
        repeat(10_000) {
            val receiveRequest = ReceiveMessageRequest.builder()
                .queueUrl(url)
                .waitTimeSeconds(20)
                .maxNumberOfMessages(MAX_NUMBER_OF_MESSAGES)
                .visibilityTimeout(300)
                .build()

            val messages = sqsAsyncClient.receiveMessage(receiveRequest).await().messages()

            if (messages.isEmpty()) {
                println("receive message done")
                return
            }

            messages.forEach {
                channel.send(
                    SqsMessage(
                        body = it.body(),
                        messageId = it.messageId(),
                        receiptHandle = it.receiptHandle()
                    )
                )
            }
        }

        // repeatを超えたらここにくる
        println("receive message done")
    }

    suspend fun process(
        receiveChannel: ReceiveChannel<SqsMessage>,
        sendChannel: SendChannel<SqsMessage>,
    ) {
        for (message in receiveChannel) {
            // TODO do something... (withContextかpoolContextでThread切り替えをする)

            // set delete queue
            sendChannel.send(message)
        }

        println("process done")
    }

    suspend fun delete(
        url: String,
        receiveChannel: ReceiveChannel<SqsMessage>,
    ) {
        val entries = mutableListOf<DeleteMessageBatchRequestEntry>()
        for (message in receiveChannel) {
            entries += DeleteMessageBatchRequestEntry.builder()
                .id(message.messageId)
                .receiptHandle(message.receiptHandle)
                .build()

            if (entries.size >= MAX_NUMBER_OF_MESSAGES) {
                val result = sqsAsyncClient.deleteMessageBatch(
                    DeleteMessageBatchRequest.builder()
                        .queueUrl(url)
                        .entries(entries)
                        .build()
                ).await()

                deleteLog(result)
                entries.clear()
            }
        }

        if (entries.isNotEmpty()) {
            val result = sqsAsyncClient.deleteMessageBatch(
                DeleteMessageBatchRequest.builder()
                    .queueUrl(url)
                    .entries(entries)
                    .build()
            ).await()
            deleteLog(result)
        }

        println("delete done")
    }

    private fun deleteLog(result: DeleteMessageBatchResponse) {
        result.failed().forEach {
            // log
            println(it.message())
        }

        result.successful().forEach {
            // debug
            println(it.id())
        }
    }

    companion object {
        private const val MAX_NUMBER_OF_MESSAGES = 10
    }

}
