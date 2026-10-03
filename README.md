# About
<<<<<<< HEAD

A micro library of functions to work with japanese strings. Now for Scala 3.3 LTS.
=======
<p align="center"><img height="500" alt="jpnutils" src="https://github.com/user-attachments/assets/f169c2c0-45b7-4b74-bdef-2649c527ab49" /></p>
<p align="center">A micro library of functions to work with japanese strings.</p>
>>>>>>> 2b40acb57d2ccc62980900d7a084a7fd8ad6c4f0

```scala
package main

import scala.language.postfixOps
import lib.{HalfWidthConverter, JapaneseUtils, Punctuation}
import lib.Givens.{*, given}
import lib.Script.*

@main def run() : Unit =
  //using the JapaneseUtils singleton
  println(JapaneseUtils.containsHiragana("込める")) //true
  println(JapaneseUtils.containsKatakana("淋しい")) //false
  println(JapaneseUtils.containsKanji("淋しい"))    //true

  println(JapaneseUtils.isHiragana('込')) //false
  println(JapaneseUtils.isKatakana('淋')) //false
  println(JapaneseUtils.isKanji('い'))    //false

  //using implicits

  //hasX methods, works on string
  println("当てのない僕は".hasHiragana) //true
  println("満月".hasKanji)         //true
  println("オカエリナサイ".hasKanji)    //false

  //isX, works on char
  println('そ'.isHiragana) //true
  println('た' isKatakana) //false
  println('林'.isKanji)    //true

  //miscellaneous methods
  val testStr = """"this is a test!? yes? it is sir.""""
  val strWithReplacedPunctuation = Punctuation.replacePunctuation(testStr)
  println(strWithReplacedPunctuation) //"this is a test！？ yes？ it is sir。"

  //「wrap me in single quotes」 and 『wrap me in double quotes』
  println(
    Punctuation.wrapInSingleQuotes("wrap me in single quotes")
  )
  println(
    Punctuation.wrapInDoubleQuotes("wrap me in double quotes")
  )

  //2025 update　KanaDiacritics
  //true
  println("俺はテストだぞ".hasDakuten)
  //true
  println("いっぱいに静かがっぽい".hasHandakuten)

  val s = "カタカナ　ＡＢＣ１２３＆％"
  println(HalfWidthConverter.toHalfWidth(s))

  //2025 update script usage
  val script = "日本語abcカナ"
  println(script.containsOnly(Set(JpnScript.Kanji, JpnScript.Katakana)) ) // false
  val summary = script.scriptSummary
  println(summary.hiragana ) // 0
  println(summary.katakana ) // 2
  println(summary.kanji    ) // 3
  println(summary.other    ) // 3

```
