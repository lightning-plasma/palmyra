package com.architype.palmyra.runner

import com.architype.palmyra.converter.MyConverter
import java.io.BufferedWriter
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path

fun main() {
    // U+FF5E などが含まれる文字列
    val source = "スチー～ル製￡軽中∥量～中量－棚（～部品・オ￠プ￠ション~）スチー～ル製￡軽中∥量～中量－棚（～部品・オ￠プ￠ション~）スチー～ル製￡軽中∥量～中量－棚（～部品・オ￠プ￠ション~）"

    val writer: BufferedWriter =
        Files.newBufferedWriter(Path.of("/tmp/foo.txt"), Charset.forName("Shift_JIS"))

    val converted = MyConverter.convertEach(source)
    writer.write(converted)
    writer.close()
}