import org.scalatest.{FlatSpec, Matchers}

class ExampleSuite extends FlatSpec with Matchers {
  val numbers = List(2, 4, 5, 6)

  it should "check elements" in {
    numbers should contain theSameElementsAs Set(4, 2, 5, 6)
    numbers should have length 4
  }

  it should "check string" in {
    "dsl rocks" should (startWith("dsl") and endWith("rocks"))
  }
}
