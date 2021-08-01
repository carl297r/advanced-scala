package lectures.part1as

/** @author carl
  *         2021-08-03 00:40
  */
object AdvancedPatternMatching extends App {

  val numbers = List(1)

  val description = numbers match {
    case head :: Nil => println(s"the only element is $head.")
    case _           =>
  }

  /*
    - constants
    - wildcards
    - case classes
    - tuples
    - some special magic like above
   */
  val bob = new Person("Bob", 25)
  val greeting = bob match {
    case Person(n, a) => s"Hi, my name is $n and I am $a yo."
  }
  val legalStatus = bob.age match {
    case Person(status) => s"My legal status is $status"
  }
  val n: Int = 4

  println(greeting)
  val matchProperty = n match {
    case x if x < 10     => "single digit"
    case x if x % 2 == 0 => "an even number"
    case _               => "no property"
  }

  println(legalStatus)

  /*
    Exercise.
   */
  val matchPropertyAlt = n match {
    case singleDigit() => "single digit"
    case even()        => "an even number"
    case _             => "no property"
  }
  val either = Or(2, "two")

  println(matchProperty)
  val humanDescription = either match {
    case number Or string => s"$number is written as $string"
  }
  // decomposing sequences
  val vararg = numbers match {
    case 1 :: tail   => "another 1"
    case List(1, _*) => "starting with 1"
  }
  val myList: MyList[Int] = Cons(1, Cons(2, Cons(3, Empty)))

  println(matchPropertyAlt)
  val decomposed = myList match {
    case MyList(1, 2, _*) => "starting wiht 1, 2"
    case _                => "something else"
  }

  abstract class MyList[+A] {
    def head: A = ???
    def tail: MyList[A] = ???
  }

  abstract class Wrapper[T] {
    def isEmpty: Boolean
    def get: T
  }
  println(humanDescription)

  class Person(val name: String, val age: Int)

  println(vararg)

  // infix patterns
  case class Or[A, B](a: A, b: B)

  case class Cons[+A](override val head: A, override val tail: MyList[A])
      extends MyList[A]

  object Person {
    def unapply(person: Person): Option[(String, Int)] =
      if (person.age < 21) None
      else Some(person.name, person.age)

    def unapply(age: Int): Option[String] =
      Some(if (age < 21) "minor" else "major")
  }

  object singleDigit {
    def unapply(n: Int): Boolean = n > -10 && n < 10
  }

  object even {
    def unapply(n: Int): Boolean = n % 2 == 0
  }

  case object Empty extends MyList[Nothing]

  println(decomposed)

  // custom return types for unapply
  // isEmpty: Boolean, get: something

  object MyList {
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] =
      if (list == Empty) Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _)
  }

  object PersonWrapper {
    def unapply(person: Person): Wrapper[String] =
      new Wrapper[String] {
        def isEmpty: Boolean = false
        def get: String = person.name
      }
  }

  println(bob match {
    case PersonWrapper(n) => s"This person's name is $n"
    case _                => "An alien"
  })

}
