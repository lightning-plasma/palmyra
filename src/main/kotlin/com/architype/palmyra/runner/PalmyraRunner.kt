package com.architype.palmyra.runner

import com.architype.palmyra.entity.Book
import com.architype.palmyra.entity.Isbn
import com.architype.palmyra.repository.BookRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

// https://kotlinlang.org/docs/channels.html#fan-out
@Component
class PalmyraRunner(
    private val bookRepository: BookRepository
) : ApplicationRunner {
    override fun run(args: ApplicationArguments) = runBlocking {
        val bookChannel = Channel<Book>()

        produceBook(bookChannel)

        // fan-out
        repeat(5) {
            launchProcessSuccess(bookChannel)
        }
        // return@runBlocking
    }

    fun CoroutineScope.produceBook(
        bookChannel: SendChannel<Book>
    ) = launch {
        val jobs = mutableListOf<Deferred<Any>>()
        bookRepository.findAll().collect {
            val book = Book(Isbn(it.isbn), it.title, it.author, it.price)

            jobs += async {
                bookChannel.send(book)
            }
        }

        // すべてのchannel sendの終了まで待つ
        println("Rest work is ${jobs.size}")
        if (jobs.isNotEmpty()) jobs.awaitAll()

        bookChannel.close()
    }

    // fan-outで実行する処理を記述する
    fun CoroutineScope.launchProcessSuccess(channel: ReceiveChannel<Book>) =
        launch {
            for (b in channel) {
                println(b)
            }
        }
}
