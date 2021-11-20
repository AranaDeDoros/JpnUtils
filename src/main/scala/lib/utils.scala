package lib

import scala.language.implicitConversions

object jpnImplicits {

  implicit class jpnStrOps(s: String) {
    def hasHiragana: Boolean = {
      JapaneseUtils.containsHiragana(s)
    }

    def hasKatakana: Boolean = {
      JapaneseUtils.containsKatakana(s)
    }

    def hasKanji: Boolean = {
      JapaneseUtils.containsKanji(s)
    }

    def wrapInSingleQuotes: String = {
      JapaneseUtils.Punctuation.wrapInSingleQuotes(s)
    }

    def wrapInDoubleQuotes: String = {
      JapaneseUtils.Punctuation.wrapInDoubleQuotes(s)
    }
  }

  implicit class jpnCharOps(c: Char) {
    def isHiragana: Boolean = {
      JapaneseUtils.isHiragana(c)
    }

    def isKatakana: Boolean = {
      JapaneseUtils.isKatakana(c)
    }

    def isKanji: Boolean = {
      JapaneseUtils.isKanji(c)
    }
  }
}

object JapaneseUtils {

  import scala.util.matching._
  import scala.collection.immutable.HashMap

  final val jpnUnicodeBounds: HashMap[String, Long] = HashMap(
    ("HIRAGANA_UPPER" -> 12447L),
    ("HIRAGANA_LOWER" -> 12352L),
    ("KATAKANA_UPPER" -> 12543L),
    ("KATAKANA_LOWER" -> 12448L),
    ("KANJI_UPPER"    -> 40879L),
    ("KANJI_LOWER"    -> 19968L)
  )

  private final val str2Long: String => Long = (s: String) =>
    s.foldLeft(1L)((l: Long, c: Char) => l * c)

  val isCharHiragana: String => Boolean = (str: String) => {
    val strAsLong: Long = this.str2Long(str)
    (strAsLong >= jpnUnicodeBounds("HIRAGANA_LOWER")
    && strAsLong <= jpnUnicodeBounds("HIRAGANA_UPPER"))
  }
  val isCharKatana: String => Boolean = (str: String) => {
    val strAsLong = this.str2Long(str)
    (strAsLong >= jpnUnicodeBounds("KATAKANA_LOWER")
    && strAsLong <= jpnUnicodeBounds("KATAKANA_UPPER"))
  }

  val isCharKanji: String => Boolean = (str: String) => {
    val strAsLong = this.str2Long(str)
    (strAsLong >= jpnUnicodeBounds("KANJI_LOWER")
    && strAsLong <= jpnUnicodeBounds("KANJI_UPPER"))
  }

  def isHiragana(char: Char): Boolean = {
    val toStr = char.toString
    this.isCharHiragana(toStr)
  }

  def isKatakana(char: Char): Boolean = {
    val toStr = char.toString
    this.isCharKatana(toStr)
  }

  def isKanji(char: Char): Boolean = {
    val toStr = char.toString
    this.isCharKanji(toStr)
  }

  def containsHiragana(str: String): Boolean = {
    val regex = ".*([\u3040-\u309F]+).*".r
    regex.matches(str)
  }

  def containsKatakana(str: String): Boolean = {
    val regex = ".*([\u30A0-\u30FF]+).*".r
    regex.matches(str)
  }

  def containsKanji(str: String): Boolean = {
    val regex = ".*([\u4E00-\u9FAF]+).*".r
    regex.matches(str)
  }

  object Punctuation {

    def replacePunctuation(str: String): String = {
      str
        .replace(",", "、")
        .replace(".", "。")
        .replace("?", "？")
        .replace("!", "！")
        .replace("(", "（")
        .replace(")", "）")

    }

    val wrapInSingleQuotes = (s: String) => s.mkString("「", "", "」")
    val wrapInDoubleQuotes = (s: String) => s.mkString("『", "", "』")

  }

}
