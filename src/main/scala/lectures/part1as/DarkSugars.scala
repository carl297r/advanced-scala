package lectures.part1as

import scala.util.Try

/** @author carl
  *         2021-08-01 20:20
  */
object DarkSugars extends App {

  // syntax sugar #1: methofs with single param
  def singleArgMethod(arg: Int): String = s"$arg little ducks..."

  val description = singleArgMethod {
    // write some complex code
    42
  }

  println(description)

  val aTryInstance = Try { // java's try {...}
    throw new RuntimeException
  }

  List(1, 2, 3).map { x =>
    x + 1
  }

  // synax sugar #2: single abstract method pattern
  trait Action {
    def act(x: Int): Int
  }

  val anInstance: Action = new Action {
    def act(x: Int): Int = x + 1
  }

  val aFunkyInstance: Action = (x: Int) => x + 1 // magic

  // example: Runnables
  val aThread = new Thread(new Runnable {
    def run(): Unit = println("hello, Scala")
  })

  val aSweeterThread = new Thread(() => println("sweet, Scala!"))

  abstract class AnAbstractType {
    def implemented: Int = 23
    def f(a: Int): Unit
  }

  val anAbstractInstance: AnAbstractType = (a: Int) => println("sweet")

  // suntax sugar #3: the :: and #:: methods are spcial

  val prependedList = 2 :: List(3, 4)
  // 2.::List(3, 4) - NO
  // List(3, 4).::(2)
  // ?!

  // scala spec: last char decides associativity of method, if : then right associative
  1 :: 2 :: 3 :: List(4, 5)
  List(4, 5).::(3).::(2).::(1) //equivalent

  class MyStream[T] {
    def -->:(value: T): MyStream[T] = this // actual implementation here
  }

  val myStream = 1 -->: 2 -->: 3 -->: new MyStream[Int]

  // syntax suger #4: multi-work method naming

  class TennGirl(name: String) {
    def `and then said`(gossip: String): Unit = println(s"$name said $gossip")
  }

  val lilly = new TennGirl("Lilly")
  lilly `and then said` "Scala is so sweet!"

  // syntax sugar #5: infix types
  class Composite[A, B]
  val composite: Int Composite String = ???

  class -->[A, B]
  val towards: Int --> String = ???

  // syntax sugar #6: update() is very special, much like apply()
  val anArray = Array(1, 2, 3)
  anArray(2) = 7 // rewritten
  anArray.update(2, 7)
  // used in mutable collections
  // remember apply() and update()

  // syntax sugar #7: setters for mutable containers
  class Mutable {
    private var internalMember: Int = 0                      // private for OO encapsulation
    def member: Int                 = internalMember         // "getter"
    def member_=(value: Int): Unit  = internalMember = value // "setter"
  }

  val aMutableContainer = new Mutable
  aMutableContainer.member = 42 // rewritten as
  aMutableContainer.member_=(42)

}
