package lib

import java.text.Normalizer
import scala.language.implicitConversions

/**
 * Implicits to inject into String and Char types
 */
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

    def hasDakuten: Boolean = {
      JapaneseUtils.containsDakuten(s)
    }

    def hasHandakuten: Boolean = {
      JapaneseUtils.containsHandakuten(s)
    }

    def wrapInSingleQuotes: String = {
      Punctuation.wrapInSingleQuotes(s)
    }

    def wrapInDoubleQuotes: String = {
      Punctuation.wrapInDoubleQuotes(s)
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

  implicit def strToLong(s: String): Long =
    s.foldLeft(1L)((l, c) => l * c)
}

/**
 * core library
 */
object JapaneseUtils {

  import scala.collection.immutable.HashMap

  private final val jpnUnicodeBounds: HashMap[String, Long] = HashMap(
    ("HIRAGANA_UPPER" -> 12447L),
    ("HIRAGANA_LOWER" -> 12352L),
    ("KATAKANA_UPPER" -> 12543L),
    ("KATAKANA_LOWER" -> 12448L),
    ("KANJI_UPPER"    -> 40879L),
    ("KANJI_LOWER"    -> 19968L)
  )

  import jpnImplicits._

  private val isCharHiragana: String => Boolean = (str: String) => {
    val strAsLong: Long = str
    (strAsLong >= jpnUnicodeBounds("HIRAGANA_LOWER")
    && strAsLong <= jpnUnicodeBounds("HIRAGANA_UPPER"))
  }

  private val isCharKatana: String => Boolean = (str: String) => {
    val strAsLong = str
    (strAsLong >= jpnUnicodeBounds("KATAKANA_LOWER")
    && strAsLong <= jpnUnicodeBounds("KATAKANA_UPPER"))
  }

  private val isCharKanji: String => Boolean = (str: String) => {
    val strAsLong = str
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

  def containsDakuten(str: String): Boolean = {
    KanaDiacritics.hasDakuten(str)
  }

  def containsHandakuten(str: String): Boolean = {
    KanaDiacritics.hasHandakuten(str)
  }

}

/**
 * for handling simple punctuation
 */
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

  val wrapInSingleQuotes: String => String = (s: String) =>
    s.mkString("「", "", "」")
  val wrapInDoubleQuotes: String => String = (s: String) =>
    s.mkString("『", "", "』")

}

/**
 * object to detect dakuten and handakuten
 */
object KanaDiacritics {
  private val Dakuten: Char    = '\u3099'
  private val Handakuten: Char = '\u309A'

  def hasDakuten(s: String): Boolean = {
    val norm = Normalizer.normalize(s, Normalizer.Form.NFD)
    norm.contains(Dakuten)
  }

  def hasHandakuten(s: String): Boolean = {
    val norm = Normalizer.normalize(s, Normalizer.Form.NFD)
    norm.contains(Handakuten)
  }
  def hasAny(s: String): Boolean = {
    val norm = Normalizer.normalize(s, Normalizer.Form.NFD)
    norm.exists(c => c == Dakuten || c == Handakuten)
  }

}



object HalfWidthConverter {

  // fullwidth → halfwidth
  private val kanaMap: Map[Char, String] = Map(
    'ア' -> "ｱ", 'イ' -> "ｲ", 'ウ' -> "ｳ", 'エ' -> "ｴ", 'オ' -> "ｵ",
    'カ' -> "ｶ", 'キ' -> "ｷ", 'ク' -> "ｸ", 'ケ' -> "ｹ", 'コ' -> "ｺ",
    'サ' -> "ｻ", 'シ' -> "ｼ", 'ス' -> "ｽ", 'セ' -> "ｾ", 'ソ' -> "ｿ",
    'タ' -> "ﾀ", 'チ' -> "ﾁ", 'ツ' -> "ﾂ", 'テ' -> "ﾃ", 'ト' -> "ﾄ",
    'ナ' -> "ﾅ", 'ニ' -> "ﾆ", 'ヌ' -> "ﾇ", 'ネ' -> "ﾈ", 'ノ' -> "ﾉ",
    'ハ' -> "ﾊ", 'ヒ' -> "ﾋ", 'フ' -> "ﾌ", 'ヘ' -> "ﾍ", 'ホ' -> "ﾎ",
    'マ' -> "ﾏ", 'ミ' -> "ﾐ", 'ム' -> "ﾑ", 'メ' -> "ﾒ", 'モ' -> "ﾓ",
    'ヤ' -> "ﾔ", 'ユ' -> "ﾕ", 'ヨ' -> "ﾖ",
    'ラ' -> "ﾗ", 'リ' -> "ﾘ", 'ル' -> "ﾙ", 'レ' -> "ﾚ", 'ロ' -> "ﾛ",
    'ワ' -> "ﾜ", 'ヲ' -> "ｦ", 'ン' -> "ﾝ",
    'ァ' -> "ｧ", 'ィ' -> "ｨ", 'ゥ' -> "ｩ", 'ェ' -> "ｪ", 'ォ' -> "ｫ",
    'ッ' -> "ｯ", 'ャ' -> "ｬ", 'ュ' -> "ｭ", 'ョ' -> "ｮ",

    // dakuten
    'ガ' -> "ｶﾞ", 'ギ' -> "ｷﾞ", 'グ' -> "ｸﾞ", 'ゲ' -> "ｹﾞ", 'ゴ' -> "ｺﾞ",
    'ザ' -> "ｻﾞ", 'ジ' -> "ｼﾞ", 'ズ' -> "ｽﾞ", 'ゼ' -> "ｾﾞ", 'ゾ' -> "ｿﾞ",
    'ダ' -> "ﾀﾞ", 'ヂ' -> "ﾁﾞ", 'ヅ' -> "ﾂﾞ", 'デ' -> "ﾃﾞ", 'ド' -> "ﾄﾞ",
    'バ' -> "ﾊﾞ", 'ビ' -> "ﾋﾞ", 'ブ' -> "ﾌﾞ", 'ベ' -> "ﾍﾞ", 'ボ' -> "ﾎﾞ",

    // handakuten
    'パ' -> "ﾊﾟ", 'ピ' -> "ﾋﾟ", 'プ' -> "ﾌﾟ", 'ペ' -> "ﾍﾟ", 'ポ' -> "ﾎﾟ",

    // Special marks
    'ヴ' -> "ｳﾞ",
    'ヵ' -> "ｶ",   // small ka
    'ヶ' -> "ｹ",   // small ke
    '・' -> "･",
    'ー' -> "ｰ",
    '「' -> "｢",
    '」' -> "｣",
    '、' -> "､",
    '。' -> "｡"
  )

  def toHalfWidth(str: String): String =
    str.flatMap { c =>
      // ASCII full-width block FF01–FF5E
      if (c >= '\uFF01' && c <= '\uFF5E')
        ((c - 0xFEE0).toChar).toString

      // Katakana map
      else if (kanaMap.contains(c))
        kanaMap(c)

      else
        (c).toString
    }
}
