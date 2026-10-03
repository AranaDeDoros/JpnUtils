package main

import scala.language.postfixOps
import lib.{HalfWidthConverter, JapaneseUtils, Punctuation}
import lib.Givens.{*, given}
import lib.Script.*
import lib.IdsNode
import lib.IdsParser

@main def run(): Unit =
  // using the JapaneseUtils singleton
  println(JapaneseUtils.containsHiragana("込める")) // true
  println(JapaneseUtils.containsKatakana("淋しい")) // false
  println(JapaneseUtils.containsKanji("淋しい"))    // true

  println(JapaneseUtils.isHiragana('込')) // false
  println(JapaneseUtils.isKatakana('淋')) // false
  println(JapaneseUtils.isKanji('い'))    // false

  // using implicits

  // hasX methods, works on string
  println("当てのない僕は".hasHiragana) // true
  println("満月".hasKanji)         // true
  println("オカエリナサイ".hasKanji)    // false

  // isX, works on char
  println('そ'.isHiragana) // true
  println('た' isKatakana) // false
  println('林'.isKanji)    // true

  // miscellaneous methods
  val testStr                    = """"this is a test!? yes? it is sir.""""
  val strWithReplacedPunctuation = Punctuation.replacePunctuation(testStr)
  println(strWithReplacedPunctuation) // "this is a test！？ yes？ it is sir。"

  // 「wrap me in single quotes」 and 『wrap me in double quotes』
  println(
    Punctuation.wrapInSingleQuotes("wrap me in single quotes")
  )
  println(
    Punctuation.wrapInDoubleQuotes("wrap me in double quotes")
  )

  // 2025 update　KanaDiacritics
  // true
  println("俺はテストだぞ".hasDakuten)
  // true
  println("いっぱいに静かがっぽい".hasHandakuten)

  val s = "カタカナ　ＡＢＣ１２３＆％"
  println(HalfWidthConverter.toHalfWidth(s))

  // 2025 update script usage
  val str = "日本語abcカナ"
  println(str.containsOnly(Set(JpnScript.Kanji, JpnScript.Katakana))) // false
  val summary = str.scriptSummary
  println(summary.hiragana) // 0
  println(summary.katakana) // 2
  println(summary.kanji)    // 3
  println(summary.other)    // 3

  // ids parser usage
  val idsInput = "⿰言⿱五口"

  IdsParser.parseIterative(idsInput) match
    case Some(tree) =>
      println("--- Árbol Generado ---")
      println(tree)
      println("\n--- Representación Formateada ---")
      println(formatTree(tree))
    case None =>
      println("Error al parsear la secuencia IDS.")

  def formatTree(node: IdsNode, indent: String = "\t"): String =
    node match
      case IdsNode.Component(value) =>
        s"${indent}Componente: $value\n"
      case IdsNode.Operation(op, children) =>
        val current = s"${indent}Operador: $op\n"
        val formattedChildren = children
          .map(c => formatTree(c, indent + "  "))
          .mkString
        current + formattedChildren
