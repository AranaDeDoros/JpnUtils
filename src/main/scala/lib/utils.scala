package lib

import Givens.{*, given}

/**
 * Core utils for Japanese text processing
 */
object JapaneseUtils:

  import scala.collection.immutable.HashMap

  private final val jpnUnicodeBounds: HashMap[String, Long] = HashMap(
    ("HIRAGANA_UPPER" -> 12447L),
    ("HIRAGANA_LOWER" -> 12352L),
    ("KATAKANA_UPPER" -> 12543L),
    ("KATAKANA_LOWER" -> 12448L),
    ("KANJI_UPPER"    -> 40879L),
    ("KANJI_LOWER"    -> 19968L)
  )



  private val isCharHiragana: String => Boolean = (str: String) =>
    val strAsLong: Long = str
    (strAsLong >= jpnUnicodeBounds("HIRAGANA_LOWER")
    && strAsLong <= jpnUnicodeBounds("HIRAGANA_UPPER"))

  private val isCharKatana: String => Boolean = (str: String) =>
    val strAsLong: Long = str
    (strAsLong >= jpnUnicodeBounds("KATAKANA_LOWER")
    && strAsLong <= jpnUnicodeBounds("KATAKANA_UPPER"))

  private val isCharKanji: String => Boolean = (str: String) =>
    val strAsLong: Long = str
    (strAsLong >= jpnUnicodeBounds("KANJI_LOWER")
    && strAsLong <= jpnUnicodeBounds("KANJI_UPPER"))

  def isHiragana(char: Char): Boolean =
    val toStr = char.toString
    this.isCharHiragana(toStr)

  def isKatakana(char: Char): Boolean =
    val toStr = char.toString
    this.isCharKatana(toStr)

  def isKanji(char: Char): Boolean =
    val toStr = char.toString
    this.isCharKanji(toStr)

  def containsHiragana(str: String): Boolean =
    val regex = ".*([\u3040-\u309F]+).*".r
    regex.matches(str)

  def containsKatakana(str: String): Boolean =
    val regex = ".*([\u30A0-\u30FF]+).*".r
    regex.matches(str)

  def containsKanji(str: String): Boolean =
    val regex = ".*([\u4E00-\u9FAF]+).*".r
    regex.matches(str)

  def containsDakuten(str: String): Boolean =
    KanaDiacritics.hasDakuten(str)

  def containsHandakuten(str: String): Boolean =
    KanaDiacritics.hasHandakuten(str)

