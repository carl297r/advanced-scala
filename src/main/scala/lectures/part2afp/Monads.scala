package lectures.part2afp

/** @author carl
  * 2021-08-14 15:02
  */
object Monads extends App {

  // out own Try monad
  trait Attempt[+A] {
    def flatMap[B](f: A => Attempt[B])

  }
  object Attempt {
    def apply[A](a: => A): Attempt[A] =
      try {
        Success(a)
      } catch {
        case e: Throwable => Failure(e)
      }
  }

  case class Success[+A](value: A) extends Attempt[A] {
    def flatMap[B](f: A => Attempt[B]): Unit =
      try {
        f(value)
      } catch {
        case e: Throwable => Failure(e)
      }
  }

  case class Failure(e: Throwable) extends Attempt[Nothing] {
    def flatMap[B](f: Nothing => Attempt[B]): Unit = this
  }

  val attempt = Attempt {
    throw new RuntimeException("My own mondad, yes!")
  }
  println(attempt)

  class Lazy[+A](a: => A) {
    // call by need
    private lazy val internalValue = a
    def use: A = internalValue
    def flatMap[B](f: (=> A) => Lazy[B]): Lazy[B] = f(internalValue)
  }
  object Lazy {
    def apply[A](a: => A): Lazy[A] = new Lazy(a)
  }

  val lazyInstance = Lazy {
    println("lazy!")
    5 + 3
  }

  //println(lazyInstance.use)

  val fmapLazy = lazyInstance.flatMap(x => Lazy(x * 10))
  val fmapLazy2 = lazyInstance.flatMap(x => Lazy(x * 10))

  println(fmapLazy.use)

  println(fmapLazy2.use)

//  trait Monad[A] {
//    def flatMap[B](f: A => Monad[B]): Monad[B] = flatMap(x => unit(f))
//
//    def map[B](f: A => B): Monad[B] =
//    def flatten(m: Monad[Monad[A]]): Monad[A] = m.flatMap(x => x)
//  }

}
