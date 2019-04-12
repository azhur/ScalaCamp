package org.azhur.scala.enumerations

object AlternativeEnum extends App {
  object Season {
    sealed trait Season
    case object Spring extends Season
    case object Summer extends Season
    case object Autumn extends Season
    case object Winter extends Season
    val values = Seq(Spring, Summer, Autumn, Winter)
  }

  import Season._

  def isCold(season: Season): Boolean = season match {
    case Winter => true
  }
}
