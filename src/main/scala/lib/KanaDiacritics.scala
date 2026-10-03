package lib

import java.text.Normalizer

/** Object to detect dakuten and handakuten **/
object KanaDiacritics:
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




