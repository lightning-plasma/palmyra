package com.architype.palmyra.runner

import com.architype.palmyra.converter.MyConverter
import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.BenchmarkMode
import org.openjdk.jmh.annotations.Mode
import org.openjdk.jmh.annotations.OutputTimeUnit
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import java.util.concurrent.TimeUnit

@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
open class PalmyraBenchMaker {
    private val source = "スチー～ル製￡軽中∥量～中量－棚（～部品・オ￠プ￠ション~）スチー～ル製￡軽中∥量～中量－棚（～部品・オ￠プ￠ション~）スチー～ル製￡軽中∥量～中量－棚（～部品・オ￠プ￠ション~）"

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    fun convertByTransliterator() {
        val converted = MyConverter.convert(source)
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    fun convertByMap() {
        val converted = MyConverter.convertEach(source)
    }
}