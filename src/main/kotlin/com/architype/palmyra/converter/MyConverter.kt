package com.architype.palmyra.converter

import com.ibm.icu.text.Transliterator

object MyConverter {
    private val map = mapOf(
        "\uFF5E" to "\u301C", // ～（ウェーブダッシュ）
        "\uFF0D" to "\u2212", // －（全角マイナス）
        "\uFFE0" to "\u00A2", // ￠（セント）
        "\uFFE1" to "\u00A3", // ￡（ポンド）
        "\uFFE2" to "\u00AC", // ￢（ノット）
        "\u2015" to "\u2014", // ―（全角マイナスより少し幅のある文字）
        "\u2225" to "\u2016", // ∥（半角パイプが2つ並んだような文字）
    )

    // 変換するべき文字があるかチェックする正規表現
    // regex.containsMatchIn(text)
    // private val regex = Regex(map.keys.joinToString("", "[", "]"))

    // 変換ルール >> (～ > 〜; － > −; ￠ > ¢; ￡ > £; ￢ > ¬; ― > —; ∥ > ‖)
    // https://github.com/friedrich/icu/blob/master/icu4j/main/tests/translit/src/com/ibm/icu/dev/test/translit/TransliteratorTest.java
    // 変換ルールについては上記Testをみると書き方がわかってくるかもしれない
    // Unicode (Windows) を Unicode (Unix) に変換するルール
    // http://hp.vector.co.jp/authors/VA010341/unicode/
    private val rules = map.map {
        "${it.key} > ${it.value}"
    }.joinToString("; ")

    private val transliterator = Transliterator.createFromRules(
        "windows-unicode-to-unix-unicode",
        rules,
        Transliterator.FORWARD
    )

    fun convert(source: String): String =
        transliterator.transliterate(source)

    fun convertEach(source: String): String {
        var converted = source

        map.forEach { (t, u) ->
            converted = converted.replace(t, u)
        }

        return converted
    }
}