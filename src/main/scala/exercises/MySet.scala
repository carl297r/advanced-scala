package exercises

import scala.annotation.tailrec

/** @author carl
  * 2021-08-04 23:50
  */
trait MySet[A] extends (A => Boolean) {
  /*
    Exercise - implement a functional set
   */
  def apply(element: A): Boolean = contains(element)

  def contains(element: A): Boolean
  def +(element: A): MySet[A]
  def ++(anotherSet: MySet[A]): MySet[A]
  def map[B](f: A => B): MySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B]
  def filter(predicate: A => Boolean): MySet[A]
  def foreach(f: A => Unit): Unit
  /*
    Exercise
    - removing an element
    - intersection with another set
    - difference with another ser
   */
  def -(element: A): MySet[A]
  def &(anotherSet: MySet[A]): MySet[A] // intersection
  def --(anotherSet: MySet[A]): MySet[A] // difference
  def unary_! : MySet[A]
}

class EmptySet[A] extends MySet[A] {
  def contains(element: A): Boolean = false
  def +(element: A): MySet[A] = new NonEmptySet[A](element, this)
  def ++(anotherSet: MySet[A]): MySet[A] = anotherSet
  def map[B](f: A => B): MySet[B] = new EmptySet[B]
  def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]
  def filter(predicate: A => Boolean): MySet[A] = this
  def foreach(f: A => Unit): Unit = ()
  def -(element: A): MySet[A] = this
  def &(anotherSet: MySet[A]): MySet[A] = this
  def --(anotherSet: MySet[A]): MySet[A] = this
  def unary_! : MySet[A] = MySet.instance(_ => true)
}

class PropertyBaasedSet[A](property: A => Boolean) extends MySet[A] {
  def contains(element: A): Boolean = property(element)
  def +(element: A): MySet[A] = MySet.instance(x => property(x) | x == element)
  def ++(anotherSet: MySet[A]): MySet[A] =
    MySet.instance(x => property(x) | anotherSet(x))
  def map[B](f: A => B): MySet[B] = politelyFail
  def flatMap[B](f: A => MySet[B]): MySet[B] = politelyFail
  def filter(predicate: A => Boolean): MySet[A] =
    MySet.instance(x => property(x) & predicate(x))
  def foreach(f: A => Unit): Unit = politelyFail
  def -(element: A): MySet[A] = filter(_ != element)
  def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)
  def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)
  def unary_! : MySet[A] = MySet.instance(!property(_))
  def politelyFail = throw new IllegalArgumentException(
    "Really deep rabbit hole!"
  )
}

case class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A] {
  def contains(element: A): Boolean = element == head || tail.contains(element)
  def +(element: A): MySet[A] =
    if (contains(element)) this else new NonEmptySet[A](element, this)
  def ++(anotherSet: MySet[A]): MySet[A] = tail ++ (anotherSet + head)
  def map[B](f: A => B): MySet[B] = tail.map(f) + f(head)
  def flatMap[B](f: A => MySet[B]): MySet[B] = tail.flatMap(f) ++ f(head)
  def filter(predicate: A => Boolean): MySet[A] =
    if (predicate(head))
      tail.filter(predicate) + head
    else
      tail.filter(predicate)
  def foreach(f: A => Unit): Unit = {
    f(head)
    tail.foreach(f)
  }
  def -(element: A): MySet[A] = filter(_ != element)
  def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)
  def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)

  def unary_! : MySet[A] = MySet.instance(!contains(_))
}

object MySet {
  def instance[A](property: A => Boolean) = new PropertyBaasedSet[A](property)

  def apply[A](elements: A*): MySet[A] =
    elements.foldLeft(MySet.empty[A])(_ + _)
  def empty[A]: MySet[A] = new EmptySet[A]
}

object MySetTest extends App {
  val evens = MySet(2, 4, 6, 8, 10, 12, 14, 16, 18, 20)
  val evensAnd19 = evens + 19
  val smalls = MySet(0, 1, 2, 3, 4, 5)
  val smallOrEvens = evens ++ smalls
  println(evens.contains(20)) // true
  println(evens.contains(19)) // false
  println(evensAnd19.contains(19)) // true
  println(smalls.contains(20)) // false
  println(smalls.contains(3)) // true
  println(smallOrEvens.contains(18)) // false
  println(smallOrEvens.contains(20)) // true
  println(smallOrEvens.contains(5)) // true
  println(smallOrEvens.filter(_ > 3).contains(12)) // false
  println(smallOrEvens.filter(_ > 3).contains(5)) // true
  println(smallOrEvens.filter(_ > 3).contains(3)) // false
  smallOrEvens.foreach(println)
  println
  evens.map(_ + 1).foreach(println)
  println
  evens.flatMap(x => MySet(x, x - 1)).foreach(println)

  println()
  val s = MySet(1, 2, 3)
  s + 5 ++ MySet(-1, -2) + 3 map (_ * 10) flatMap (x =>
    MySet(x, x * 10)
  ) filter (_ % 20 == 0) foreach println

  val negative = !s
  println(negative(2))
  println(negative(20))

}
