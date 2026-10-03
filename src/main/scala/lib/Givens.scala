package lib

import scala.language.implicitConversions

/**
  * Implicits to inject into String and Char types
  */
object Givens:

  given strToLong: Conversion[String, Long] =
  (s: String) => s.foldLeft(1L)((l, c) => l * c)

  extension (s: String)
    def hasHiragana: Boolean =
      JapaneseUtils.containsHiragana(s)

    def hasKatakana: Boolean =
      JapaneseUtils.containsKatakana(s)

    def hasKanji: Boolean =
      JapaneseUtils.containsKanji(s)

    def hasDakuten: Boolean =
      JapaneseUtils.containsDakuten(s)

    def hasHandakuten: Boolean =
      JapaneseUtils.containsHandakuten(s)

    def wrapInSingleQuotes: String =
      Punctuation.wrapInSingleQuotes(s)

    def wrapInDoubleQuotes: String =
      Punctuation.wrapInDoubleQuotes(s)


  extension (c: Char)
    def isHiragana: Boolean =
      JapaneseUtils.isHiragana(c)

    def isKatakana: Boolean =
      JapaneseUtils.isKatakana(c)

    def isKanji: Boolean =
      JapaneseUtils.isKanji(c)




