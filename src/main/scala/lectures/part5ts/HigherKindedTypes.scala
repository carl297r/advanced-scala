package lectures.part5ts

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/** @author carl
  * 2021-08-25 12:18
  */
object HigherKindedTypes extends App {

  trait AHigherKindedType[F[_]]

  trait MyList[T] {
    def flatMap[B](f: T => B): MyList[B]
  }

  trait MyOption[T] {
    def flatMap[B](f: T => B): MyOption[B]
  }

  trait MyFuture[T] {
    def flatMap[B](f: T => B): MyFuture[B]
  }

  // combine/multiple List(1, 2) x List("a", "b") = List(1a, 1b, 2a, 2b)

  def multiple[A, B](listA: List[A], listB: List[B]): List[(A, B)] =
    for {
      a <- listA
      b <- listB
    } yield (a, b)

  def multiple[A, B](optionA: Option[A], optionB: Option[B]): Option[(A, B)] =
    for {
      a <- optionA
      b <- optionB
    } yield (a, b)

  def multiple[A, B](futureA: Future[A], futureB: Future[B]): Future[(A, B)] =
    for {
      a <- futureA
      b <- futureB
    } yield (a, b)

  // use HKT

  trait Monad[F[_], A] { // higher-kinded type class
    def flatMap[B](f: A => F[B]): F[B]
    def map[B](f: A => B): F[B]
  }

  implicit class MonadList[A](list: List[A]) extends Monad[List, A] {
    def flatMap[B](f: A => List[B]): List[B] = list.flatMap(f)
    def map[B](f: A => B): List[B] = list.map(f)
  }

  implicit class MonadOption[A](list: Option[A]) extends Monad[Option, A] {
    def flatMap[B](f: A => Option[B]): Option[B] = list.flatMap(f)
    def map[B](f: A => B): Option[B] = list.map(f)
  }

  def multiple[F[_], A, B](implicit
      ma: Monad[F, A],
      mb: Monad[F, B]
  ): F[(A, B)] =
    for {
      a <- ma
      b <- mb
    } yield (a, b)

  val monadList = new MonadList(List(1, 2, 3))
  monadList.flatMap(x => List(x, x + 1)) // List[Int]
  // Monad[List, Int] => List[Int]
  monadList.map(_ * 2) // List[Int]
  // Monad[List, Int] => List[Int]

  println(multiple(new MonadList(List(1, 2)), new MonadList(List("a", "b"))))
  println(multiple(new MonadOption(Some(2)), new MonadOption(Some("scala"))))

  println(multiple(List(1, 2), List("a", "b")))
  println(multiple(Some(2), Some("scala")))

}
