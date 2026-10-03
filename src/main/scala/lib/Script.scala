package lib

import lib.Givens.{*, given}

/** Script analysis **/
object Script:

    enum JpnScript:
        case Higarana, Katakana, Kanji, Other

    case class JpnScriptSummary(hiragana: Int, katakana: Int,
                                kanji: Int, other: Int)

    extension(c: Char)
        def scriptOf: JpnScript =
            c match
                case c if c.isHiragana => JpnScript.Higarana
                case c if c.isKatakana => JpnScript.Katakana
                case c if c.isKanji    => JpnScript.Kanji
                case _                 => JpnScript.Other

    extension (s: String)
        def containsOnly(allowed: Set[JpnScript]): Boolean =
            !s.toSeq.exists(c => !allowed.contains(scriptOf(c)))

        def scriptSummary: JpnScriptSummary =
            s.toSeq.foldLeft(JpnScriptSummary(0, 0, 0, 0)) { (acc, c) =>
                scriptOf(c) match
                    case JpnScript.Higarana => acc.copy(hiragana = acc.hiragana + 1)
                    case JpnScript.Katakana => acc.copy(katakana = acc.katakana + 1)
                    case JpnScript.Kanji => acc.copy(kanji = acc.kanji + 1)
                    case JpnScript.Other => acc.copy(other = acc.other + 1)
            }

