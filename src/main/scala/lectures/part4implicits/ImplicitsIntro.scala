package lectures.part4implicits

/** @author carl
  * 2021-08-23 06:14
  */
object ImplicitsIntro extends App {
  val pair = "Daniel" -> "SSS"
  val intPair = 1 -> 2

  case class Person(name: String) {
    def greet = s"Hi, my name is $name"
  }

  implicit def fromStringToPerson(str: String): Person = Person(str)

  println("Peter".greet)
  println(fromStringToPerson("Peter").greet)

  // class A {
  //   def greet: Int = 2
  // }
  //
  // implicit def fromStringToA(str: String): A = new A

  def increment(x: Int)(implicit amount: Int): Int = x + amount
  implicit def defaultAmount = 10

  increment(2)
  increment(2)(5)

}
