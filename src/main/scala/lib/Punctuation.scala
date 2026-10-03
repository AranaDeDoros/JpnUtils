package lib

/**
 * For handling simple punctuation
 */
object Punctuation:

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



