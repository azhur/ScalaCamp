package org.azhur.scala.interop

trait Compiler {
  val language: String = "no_lang"

  def interop(): Unit

  def compile(filePath: String): String = {
    filePath.replace(s".$language", ".class")
  }
}