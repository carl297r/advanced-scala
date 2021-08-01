package lectures.part4implicits

import scala.language.postfixOps

/** @author carl
  * 2021-08-23 22:50
  */
object PimpMyLibrary extends App {
  // 2.isPrime

  implicit class RichInt(val value: Int) extends AnyVal {
    def isEven: Boolean = value % 2 == 0
    def sqrt: Double = math.sqrt(value)
    def times[T](f: () => T): Unit =
      1 to value foreach (_ => f())
    def *[T](l: List[T]): List[T] = 1 to value flatMap (_ => l) toList
  }

  implicit class RicherInt(richInt: RichInt) {
    def isOdd: Boolean = richInt.value % 2 != 0
  }

  new RichInt(42).sqrt
  42.isEven

  1 until 5

  import scala.concurrent.duration._

  3.seconds

  3 seconds

  RichInt(42).isOdd

  implicit class RichString(val value: String) extends AnyVal {
    def asInt: Int = Integer.valueOf(value)
    def encrypt(distance: Int): String = value.map(c => (c + distance).toChar)
  }

  println("456".asInt - 6)
  println("John".encrypt(2))
  3.times(() => println("Hello"))
  println(3 * List(1, 2))

  implicit def stringToInt(string: String): Int = Integer.valueOf(string)
  println("6" / 2)

  class RichAltInt(value: Int)
  implicit def enrich(value: Int): RichAltInt = new RichAltInt(value)

  implicit def intToBoolean(i: Int): Boolean = i == 1

  val aConditionedValue = if (3) "OK" else "Something wrong"
  println(aConditionedValue)

}
