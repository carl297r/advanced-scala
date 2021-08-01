package exercises

import scala.annotation.tailrec

/** @author carl
  * 2021-08-12 22:32
  */
/*
  Exercise: implement a lazily evaluated, single linked STREAM of elements

  naturals = MyStream.from(1)(x => x + 1) = stream of natural numbers (potentially infinite!)
  naturals.take(100).foreach(println) // lazily evaluated stream of the first 100 naturals (finite stream)
  naturals.foreach(println) // will crash - infinite steam cl-will it?
  naturals.map(_ * 2) // stream of even numbers - potentially infinite
 */

abstract class MyStream[+A] {
  def isEmpty: Boolean
  def head: A
  def tail: MyStream[A]

  def #::[B >: A](element: B): MyStream[B]
  def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B]

  def foreach(f: A => Unit): Unit
  def map[B](f: A => B): MyStream[B]
  def flatMap[B](f: A => MyStream[B]): MyStream[B]
  def filter(predicate: A => Boolean): MyStream[A]

  def take(n: Int): MyStream[A]
  def takeAsList(n: Int): List[A] = take(n).toList()

  @tailrec
  final def toList[B >: A](acc: List[B] = Nil): List[B] =
    if (isEmpty) acc.reverse
    else tail.toList(head :: acc)
}

object SNil extends MyStream[Nothing] {
  def isEmpty: Boolean = true
  def head: Nothing = throw new NoSuchElementException
  def tail: MyStream[Nothing] = throw new NoSuchElementException

  def #::[B >: Nothing](element: B): MyStream[B] = new SCons[B](element, this)
  def ++[B >: Nothing](anotherStream: => MyStream[B]): MyStream[B] =
    anotherStream

  def foreach(f: Nothing => Unit): Unit = ()
  def map[B](f: Nothing => B): MyStream[B] = this
  def flatMap[B](f: Nothing => MyStream[B]): MyStream[B] = this
  def filter(predicate: Nothing => Boolean): MyStream[Nothing] = this

  def take(n: Int): MyStream[Nothing] = this
//  def takeAsList(n: Int): List[Nothing] = Nil
}

class SCons[+A](val head: A, tailByName: => MyStream[A]) extends MyStream[A] {
  def isEmpty: Boolean = false
  lazy val tail: MyStream[A] = tailByName // call by need

  def #::[B >: A](element: B): MyStream[B] =
    new SCons[B](element, this)

  def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B] =
    new SCons[B](head, tail ++ anotherStream)

  def foreach(f: A => Unit): Unit = {
    @tailrec
    def foreachLoop(s: => MyStream[A]): Unit =
      if (s.isEmpty)
        ()
      else {
        f(s.head)
        foreachLoop(s.tail)
      }
    foreachLoop(this)
  }

  def map[B](f: A => B): MyStream[B] =
    new SCons[B](f(head), tail.map(f))

  def flatMap[B](f: A => MyStream[B]): MyStream[B] =
    f(head) ++ tail.flatMap(f)

  def filter(predicate: A => Boolean): MyStream[A] = {
//    lazy val filteredTail = tail.filter(predicate)
    if (predicate(head))
//      head #:: filteredTail
      new SCons(head, tail.filter(predicate))
    else
      tail.filter(predicate)
  }

  def take(n: Int): MyStream[A] =
    if (n <= 0)
      SNil
    else if (n == 1)
      new SCons(head, SNil)
    else
      new SCons(head, tail.take(n - 1))
  //    if (n > 0)
//      new SCons[A](head, tail.take(n - 1))
//    else
//      SNil

//  def takeAsList(n: Int): List[A] =
//    if (n > 0)
//      head :: takeAsList(n - 1)
//    else
//      Nil
}

object MyStream {
  def from[A](start: A)(generator: A => A): MyStream[A] =
    new SCons[A](start, from(generator(start))(generator))
}

object MyStreamPlayground extends App {
  val naturals = MyStream.from(1)(x => x + 1)
  naturals.take(100).foreach(println)
  val evens = naturals.map(_ * 2)
  evens.take(10).foreach(println)
  evens.takeAsList(100)

  (100 #:: 90 #:: SNil) foreach println

  100 #:: evens takeAsList 5 foreach println

  evens ++ evens map (_ * 2) map (x => (x, x + 1)) take 7 foreach println

  val concat = (1 #:: 2 #:: 3 #:: SNil) ++ (1 #:: 2 #:: 3 #:: SNil)
  concat.foreach(println)

  val startFrom0 = 0 #:: naturals
  println(startFrom0.map(_ * 2).take(100).toList())
  println(
    startFrom0
      .flatMap(x => new SCons(x, new SCons(x + 1, SNil)))
      .take(10)
      .toList()
  )
  println(startFrom0.filter(_ < 10).take(10).take(20).toList())

  new SCons(5, SNil.take(0))

  //0 1 1 2 3 5 8

  def fibb(first: BigInt, second: BigInt): MyStream[BigInt] =
    MyStream
      .from(first -> second) { case (first, second) =>
        (second, first + second)
      }
      .map(_._1)

  fibb(0, 1).take(20).foreach(println)

  def fib(first: BigInt, second: BigInt): MyStream[BigInt] =
    new SCons(first, fib(second, first + second))

  fib(0, 1).take(20).foreach(println)

  /*
   *   [2, 3, 4, 5, 6, 7, 8]
   * [2, [3, 4, 5, 6, 7] filter 2
   * [2, 3, [5, 7, 8, 9, 11, 13, 15, 17] filter 3
   * [2, 3, 5, [5, 7, 8, 9, 11, 15, 13, 17] filter 5
   * */

  def sieve(nums: MyStream[Int]): MyStream[Int] = {
    if (nums.isEmpty) nums
    else new SCons(nums.head, sieve(nums.tail).filter(_ % nums.head != 0))
  }

  val primes = sieve(naturals.tail).take(100)

  primes.foreach(println)

}
