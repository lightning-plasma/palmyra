package com.architype.palmyra.runner

import com.architype.palmyra.converter.MyConverter

fun main() {
    // U+FF5E などが含まれる文字列
    val source = "スチー～ル製￡軽中∥量～中量－棚（～部品・オ￠プ￠ション~）スチー～ル製￡軽中∥量～中量－棚（～部品・オ￠プ￠ション~）スチー～ル製￡軽中∥量～中量－棚（～部品・オ￠プ￠ション~）"

    val convert1 = MyConverter.convert(source)
    val convert2 = MyConverter.convertEach(source)

    println(convert1 == convert2)
}