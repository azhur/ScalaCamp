import scala.language.implicitConversions

// Rich my library pattern
implicit class RichStr(val s: String) {
  def encoded: String = s"##$s##"
  def decoded: String = s.stripPrefix("##").stripSuffix("##")
}

"test".encoded.decoded

// implicit methods
implicit def intToString(number: Int): String = number.toString

def strLength(str: String): Int = str.length
strLength(245)

// implicit parameters
trait Encoder {
  def encode(str: String): String
}

def encodeLine(line: String)(implicit e: Encoder): String = {
  e.encode(line)
}

implicit val noopEncoder = new Encoder {
  override def encode(str: String): String = str
}

encodeLine("test")