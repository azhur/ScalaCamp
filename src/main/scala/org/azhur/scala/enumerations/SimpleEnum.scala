package org.azhur.scala.enumerations

object SimpleEnum extends App {

  object Season extends Enumeration {
    type Season = Value
    val Spring, Summer, Autumn, Winter = Value
  }

  import Season._

  def isCold(d: Season): Boolean = d == Winter

  Season.values.filterNot(isCold).foreach(println)

  println(Season.withName("Spring"))
}
