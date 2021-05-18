package com.architype.palmyra

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PalmyraApplication

fun main(args: Array<String>) {
	runApplication<PalmyraApplication>(*args)
}
