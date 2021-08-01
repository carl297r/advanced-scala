package lectures.part2afp

import scala.annotation.tailrec

/** @author carl
  * 2021-08-06 12:52
  */
object LazyEvaluation extends App {
  // lazy delays the evaluation of values
  lazy val x: Int = {
    println("hello")
    42
  }
  println(x)
  println(x)

  // examples of implications
  // side effects
  def sideEffectFunction: Boolean = {
    println("Boo!")
    true
  }

  def simpleConsidition = false

  lazy val lazyCondition = sideEffectFunction
  println(if (simpleConsidition && lazyCondition) "yes" else "no")

  // in conduction with call by name
  def callByName(t: => Int): Int = {
    // call by need - call by name with lazy val
    lazy val n = t
    n + n + n + 1
  }

  def retrieveMagicValue: Int = {
    // side effect or a long computation
    println("waiting")
    Thread.sleep(1000)
    42
  }

  println(callByName(retrieveMagicValue))
  // use lazy vals

  //filtering with lazy vals
  def lessThan30(i: Int): Boolean = {
    println(s"$i < 30?")
    i < 30
  }

  def greaterThan20(i: Int): Boolean = {
    println(s"$i > 20?")
    i > 20
  }

  val numbers = List(1, 25, 40, 5, 23)
  val lt30 = numbers.filter(lessThan30)
  val gt20 = lt30.filter(greaterThan20)
  println(gt20)

  val lt30Lazy = numbers.withFilter(lessThan30)
  val gt20Lazy = lt30Lazy.withFilter(greaterThan20)
  println
  println(gt20Lazy)
  gt20Lazy.foreach(println)

  // for-comprehensions use withFilter with guards
  for {
    a <- List(1, 2, 3) if a % 2 == 0 //uses lazy vals!
  } yield a + 1

  List(1, 2, 3).withFilter(_ % 2 == 0)

}
