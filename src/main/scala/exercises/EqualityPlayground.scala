package exercises

import lectures.part4implicits.TypeClasses
import lectures.part4implicits.TypeClasses.User

/** @author carl
  * 2021-08-24 11:20
  */
object EqualityPlayground extends App {
  trait Equal[T] {
    def apply(a: T, b: T): Boolean
  }
  object Equal {
    def apply[T](a: T, b: T)(implicit equalizer: Equal[T]): Boolean =
      equalizer(a, b)
  }

  implicit object NameEquality extends Equal[User] {
    def apply(a: User, b: User): Boolean = a.name == b.name
  }

  object FullEquality extends Equal[User] {
    def apply(a: User, b: User): Boolean =
      a.name == b.name && a.email == b.email
  }

  val john = User("John", 32, "john@rockthejvm.com")
  val anotherJohn = User("John", 45, "anotherJohn@foo.com")

  println(Equal(john, anotherJohn))
  println(Equal(john, anotherJohn)(FullEquality))

  implicit class TypeSafeEqual[T](value: T) {
    def ===(other: T)(implicit eq: Equal[T]): Boolean = eq(value, other)
    def !=(other: T)(implicit eq: Equal[T]): Boolean = !(value === other)
  }

  println(john === anotherJohn)
  println(john != anotherJohn)

  new TypeSafeEqual[User](john).===(anotherJohn)(NameEquality)

  println(john == 43)
  // println(john === 43)
}
