package lectures.part1as

import scala.annotation.tailrec

/** @author carl
  *         2021-08-01 19:19
  */
object Recap extends App {

  val aCondition: Boolean = false
  val aConditionalVal     = if (aCondition) 42 else 65
  // instructions vs expressions

  // compiler infers types for us
  val aCodeBlock = {
    if (aCondition) 54
    56
  }

  // Unit
  val theUnit = println("hello, scala")

  // functions
  def aFunction(x: Int) = x + 1

  // recursion stack and tail
  @tailrec
  def factorial(n: Int, accumulator: Int): Int =
    if (n <= 0) accumulator
    else factorial(n - 1, n * accumulator)

  // object-oriented programming

  class Animal
  class Dog extends Animal
  val aDog: Animal = new Dog // subtyping polymorphism

  trait Carnivore {
    def eat(a: Animal): Unit
  }

  class Crocodile extends Animal with Carnivore {
    override def eat(a: Animal): Unit = println("crunch!")
  }

  // method notations
  val aCroc = new Crocodile
  aCroc.eat(aDog)
  aCroc eat aDog // natural language

  // anonymous classes
  val aCarnivore = new Carnivore {
    def eat(a: Animal): Unit = println("roar!")
  }

  // generics
  abstract class MyList[+A] // variance and variance problems in this course
  // singletons and companions
  object MyList

  // case classes
  case class Person(name: String, age: Int)

  //exceptions and try/catch/finally

  val throwsException: Nothing = throw new RuntimeException // Nothing
  val aPotentialFailure =
    try {
      throw new RuntimeException
    } catch {
      case e: Exception => "I caught an exception"
    } finally ("some logs")

  // packiaging and imports

  // functional programming
  val incrementer = new Function1[Int, Int] {
    override def apply(v1: Int): Int = v1 + 1
  }

  incrementer(1)

  val anonymousIncrementer = (x: Int) => x + 1
  List(1, 2, 3).map(anonymousIncrementer)

  // for-comprehension
  val pairs = for {
    num  <- List(1, 2, 3) // if condition
    char <- List('a', 'b', 'c')
  } yield num + "-" + char

  // Scala collection: Seqs, Arrays, Lists, Vectors, Maps, Tuples
  val aMap = Map(
    "Dan"  -> 789,
    "Jess" -> 555
  )

  // "collection": Option, Try
  val anOption = Some(2)

  // pattern matching
  val x = 2
  val order = x match {
    case 1 => "first"
    case 2 => "second"
    case 3 => "third"
    case _ => x + "th"
  }

  val bob = Person("Bob", 22)
  val greeting = bob match {
    case Person(n, _) => s"Hi, my name is $n"
  }

  // all the patterns
}
