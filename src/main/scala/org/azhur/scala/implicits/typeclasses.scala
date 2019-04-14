package org.azhur.scala.implicits

object typeclasses extends App {
  import scala.language.implicitConversions

  object overloading {
    def combine(x: Int, y: Int): Int = x + y
    def combine(x: String, y: String): String = x + y
    def combine[A](x: A, y: A): A = ???
  }

  object subtyping {
    trait Addable[A] {
      def add(other: A): A
    }

    // attempt2: A must extend Addable
    def combineS[A <: Addable[A]](x: A, y: A): A = x.add(y)

    /**
      * oops: can't access int and string to make them extend Addable
      */


    // attempt3: wiew bound: A must be convertible to an Addable.
    def combineV[A <% Addable[A]](x: A, y: A): A = x.add(y)

    implicit def intToAddable(x: Int): Addable[Int] = new Addable[Int] {
      override def add(other: Int) = x + other
    }

    implicit def stringToAddable(x: String): Addable[String] = new Addable[String] {
      override def add(other: String) = x + other
    }

    combineV("a", "b")
    combineV(2, 3)

    /**
      * drawbacks:
      *  - enforce to use implicit conversion, even if we just want to perform a single operation
      *  - not natural to define a binary operation on a single value
      */
  }

  object injection {
    // attempt4: simple param injection

    trait Adder[A] {
      def add(x: A, y: A): A
    }

    def combine[A](x: A, y: A)(adder: Adder[A]): A =
      adder.add(x, y)

    object IntAdder extends Adder[Int] {
      override def add(x: Int, y: Int) = x + y
    }

    object StringAdder extends Adder[String] {
      override def add(x: String, y: String) = x + y
    }

    combine(1, 2)(IntAdder)
    combine("abc", "xyz")(StringAdder)

    // attempt5: can we omit passing the adder?
  }
}
