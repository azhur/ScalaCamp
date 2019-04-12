package org.azhur.scala.enumerations


object BreakEnum extends App {
  object ProgrammingLanguage extends Enumeration {
    type ProgrammingLanguage = Value
    val Scala, Java, Python, JS, Go = Value
  }

  import SimpleEnum.Season._
  import ProgrammingLanguage._


  def showType(lang: ProgrammingLanguage): Unit = {
    println(s"enum: $lang")
  }

  /*def showType(season: Season): Unit = {
    println(s"enum: $season")
  }*/


  def isJvm(lang: ProgrammingLanguage): Boolean = lang match {
    case Scala | Java => true
  }

  isJvm(Go)
}
