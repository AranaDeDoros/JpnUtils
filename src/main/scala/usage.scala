package main

import scala.language.postfixOps
import lib.JapaneseUtils
import lib.jpnImplicits._

object Usage extends App {

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

  //misceallaneous methods
  val testStr = """"this is a test!? yes? it is sir.""""
  val strWithReplacedPunctionation = JapaneseUtils.Punctuation
    .replacePunctuation(testStr)
  println(strWithReplacedPunctionation) //"this is a test！？ yes？ it is sir。"

  //「wrap me in single quotes」 and 『wrap me in double quotes』
  println(
    JapaneseUtils.Punctuation.wrapInSingleQuotes("wrap me in single quotes")
  )
  println(
    JapaneseUtils.Punctuation.wrapInDoubleQuotes("wrap me in double quotes")
  )

}
