package com.architype.palmyra.runner

import com.architype.palmyra.entity.SqsMessage
import com.architype.palmyra.repository.sqs.SqsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

// https://github.com/awsdocs/aws-doc-sdk-examples/tree/master/javav2
// https://kotlinlang.org/docs/channels.html#fan-out
@Component
@ExperimentalCoroutinesApi
class PalmyraRunner(
    private val sqsRepository: SqsRepository
) : ApplicationRunner {
    override fun run(args: ApplicationArguments) = runBlocking {
        val url = sqsRepository.getUrl("category-queue-dev").await().queueUrl()

        val receiveChannel = Channel<SqsMessage>()
        val deleteChannel = Channel<SqsMessage>()

        // coroutine 1 produce
        val receiveJob = launch {
            sqsRepository.receive(url, receiveChannel)
        }

        // coroutine 2 process (fan-out)
        val processJob = launch {
            sqsRepository.process(receiveChannel, deleteChannel)
        }

        // coroutine 3 delete
        val deleteJob = launch {
            sqsRepository.delete(url, deleteChannel)
        }

        // receiveが完了したらMessageChannelを閉じる
        receiveJob.join()
        receiveChannel.close()

        // processJobが完了したらDeleteChannelを閉じる
        processJob.join()
        deleteChannel.close()

        // deleteが終わったら終了
        deleteJob.join()
        println("done")

        return@runBlocking
    }
}

// https://github.com/Kotlin/coroutines-examples/blob/master/examples/future/await.kt
// https://droidkaigi.github.io/codelabs-kotlin-coroutines-ja/
// 非同期処理の完了まで待機する
suspend fun <T> CompletableFuture<T>.await(): T =
    suspendCoroutine { cont: Continuation<T> ->
        whenComplete { result, exception ->
            if (exception == null) // the future has been completed normally
                cont.resume(result)
            else // the future has completed with an exception
                cont.resumeWithException(exception)
        }
    }
