package org.azhur.scala.implicits

import scala.language.implicitConversions

object scopes extends App {
  trait Parent {
    implicit val parentTC = TypeClass("defined in parent trait")
  }

  case class TypeClass(line: String)

  object TypeClass extends Parent {
    implicit val defaultTC = TypeClass("defined in companion object")
  }

  object Explicit {
    implicit val explicitTC = TypeClass("defined in explicit object")
  }

  object Wildcard {
    implicit val wildcardTC = TypeClass("defined in wildcard object")
  }

  def findTypeClass(implicit tc: TypeClass): TypeClass = tc

  implicit val localTC = TypeClass("defined in local scope")
  //import Explicit.explicitTC
  //import Wildcard._

  println(findTypeClass)
}
