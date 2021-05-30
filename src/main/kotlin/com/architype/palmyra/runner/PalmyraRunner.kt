package com.architype.palmyra.runner

import com.architype.palmyra.entity.Foo
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import org.springframework.util.ResourceUtils
import java.nio.file.Files
import javax.validation.Validator

@Component
class PalmyraRunner(
    private val validator: Validator
) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        val filePath = ResourceUtils.getFile("classpath:foo.csv").toPath()
        val reader = Files.newBufferedReader(filePath)
        val mapper = CsvMapper().registerModule(KotlinModule())

        val iterator = mapper
            .readerFor(Foo::class.java)
            .with(CsvSchema.emptySchema().withHeader())
            .readValues<Foo>(reader)

        iterator.forEach {
            val errors = validator.validate(it)
            if (errors.isEmpty()) {
                println(it)
            } else {
                println(errors)
            }
        }
    }
}
