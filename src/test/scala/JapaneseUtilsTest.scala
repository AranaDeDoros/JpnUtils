
import lib.{JapaneseUtils, Punctuation}
import lib.jpnImplicits._
import munit.ScalaCheckSuite
import org.scalacheck.Gen
import org.scalacheck.Prop.forAll

import scala.language.postfixOps

class JapaneseUtilsTest extends ScalaCheckSuite {


  test("containsX methods") {
    assertEquals(JapaneseUtils.containsHiragana("込める"), true)
    assertEquals(JapaneseUtils.containsKatakana("淋しい"), false)
    assertEquals(JapaneseUtils.containsKanji("淋しい"), true)
  }

  test("isX methods for chars") {
    assertEquals(JapaneseUtils.isHiragana('込'), false)
    assertEquals(JapaneseUtils.isKatakana('淋'), false)
    assertEquals(JapaneseUtils.isKanji('い'), false)
  }

  test("implicit String.hasX methods") {
    assertEquals("当てのない僕は".hasHiragana, true)
    assertEquals("満月".hasKanji, true)
    assertEquals("オカエリナサイ".hasKanji, false)
  }

  test("implicit Char.isX methods") {
    assertEquals('そ'.isHiragana, true)
    assertEquals('た'.isKatakana, false)
    assertEquals('林'.isKanji, true)
  }

  test("replace punctuation") {
    val testStr = """"this is a test!? yes? it is sir.""""
    val replaced = Punctuation.replacePunctuation(testStr)
    assertEquals(replaced, "\"this is a test！？ yes？ it is sir。\"")
  }

  test("wrap in Japanese quotes") {
    assertEquals(
      Punctuation.wrapInSingleQuotes("wrap me in single quotes"),
      "「wrap me in single quotes」"
    )

    assertEquals(
      Punctuation.wrapInDoubleQuotes("wrap me in double quotes"),
      "『wrap me in double quotes』"
    )
  }

  test("2025 KanaDiacritics updates") {
    assertEquals("俺はテストだぞ".hasDakuten, true)
    assertEquals("いっぱいに静かがっぽい".hasHandakuten, true)
  }


  // Rangos Unicode relevantes
  val hiraganaRange = '\u3041' to '\u3096'
  val katakanaRange = '\u30A1' to '\u30FA'
  val kanjiRange    = '\u4E00' to '\u9FAF'

  val genHiraganaChar: Gen[Char] = Gen.oneOf(hiraganaRange)
  val genKatakanaChar: Gen[Char] = Gen.oneOf(katakanaRange)
  val genKanjiChar:    Gen[Char] = Gen.oneOf(kanjiRange)

  val genAsciiChar: Gen[Char] = Gen.choose(32.toChar, 126.toChar)
  val genAsciiString: Gen[String] = Gen.listOf(genAsciiChar).map(_.mkString)


  property("every Hiragana char should satisfy isHiragana") {
    forAll(genHiraganaChar) { c =>
      JapaneseUtils.isHiragana(c)
    }
  }

  property("string made only of Hiragana must return hasHiragana = true") {
    val genStr = Gen.listOf(genHiraganaChar).map(_.mkString)
    forAll(genStr) { s =>
      s.isEmpty || s.hasHiragana
    }
  }


  property("every Katakana char should satisfy isKatakana") {
    forAll(genKatakanaChar) { c =>
      JapaneseUtils.isKatakana(c)
    }
  }

  property("string made only of Katakana must return hasKatakana = true") {
    val genStr = Gen.listOf(genKatakanaChar).map(_.mkString)
    forAll(genStr) { s =>
      s.isEmpty || s.hasKatakana
    }
  }


  property("every Kanji char should satisfy isKanji") {
    forAll(genKanjiChar) { c =>
      JapaneseUtils.isKanji(c)
    }
  }

  property("string made only of Kanji must return hasKanji = true") {
    val genStr = Gen.listOf(genKanjiChar).map(_.mkString)
    forAll(genStr) { s =>
      s.isEmpty || s.hasKanji
    }
  }


  property("ASCII chars should never be Hiragana/Katakana/Kanji") {
    forAll(genAsciiChar) { c =>
      !JapaneseUtils.isHiragana(c) &&
        !JapaneseUtils.isKatakana(c) &&
        !JapaneseUtils.isKanji(c)
    }
  }

  property("ASCII strings should never have Hiragana/Katakana/Kanji") {
    forAll(genAsciiString) { s =>
      !s.hasHiragana &&
        !s.hasKatakana &&
        !s.hasKanji
    }
  }
}
