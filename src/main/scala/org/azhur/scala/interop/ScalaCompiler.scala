package org.azhur.scala.interop

import java.util

class ScalaCompiler extends Compiler {
  override val language: String = "scala"

  override def interop(): Unit = {
    // collections
    val al = new util.ArrayList[Int]()
    al.add(1)
    al.add(2)
    al.add(3)

    // to tranform to/from scala collections
    import scala.collection.JavaConverters._
    val scalaCol = al.asScala.toList
    val javaCol = scalaCol.asJava
  }
}
