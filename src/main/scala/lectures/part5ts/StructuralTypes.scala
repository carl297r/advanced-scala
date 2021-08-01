package lectures.part5ts

/** @author carl
  * 2021-08-25 00:04
  */
object StructuralTypes extends App {

  type JavaCloseable = java.io.Closeable

  class HipsterCloseable {
    def close(): Unit = println("yeah yeah I'm closing")
    def closeSilently(): Unit = println("Not making a sound")
  }

  // def closeQuietly(closeable: JavaCloseable or HipsterCloseable)

  type UnifiedClosable = {
    def close(): Unit
  } // Structural Type

  def closeQuietly(unifiedClosable: UnifiedClosable): Unit =
    unifiedClosable.close()

  closeQuietly(new JavaCloseable {
    def close(): Unit = println("yes!")
  })
  closeQuietly(new HipsterCloseable)

  // Type Refinements

  type AdvancedCloseable = JavaCloseable {
    def closeSilently(): Unit
  }

  class AdvancedJavaClosable extends JavaCloseable {
    def close(): Unit = println("Java closes")
    def closeSilently(): Unit = println("Java closes silently")
  }

  def closeShh(advancedCloseable: AdvancedCloseable): Unit =
    advancedCloseable.closeSilently()

  closeShh(new AdvancedJavaClosable)
  // closeShh(new HipsterCloseable)

  def alternativeClose(closeable: { def close(): Unit }): Unit =
    closeable.close()

  alternativeClose(new HipsterCloseable)
  alternativeClose(new AdvancedJavaClosable)

  type SoundMaker = {
    def makeSound(): Unit
  }

  class Dog {
    def makeSound(): Unit = println("bark!")
  }

  class Car {
    def makeSound(): Unit = println("vrooom!")
  }

  val dog: SoundMaker = new Dog
  val car: SoundMaker = new Car

  // static duck typing

  // Caveat: based on reflection - performance impact

  trait CBL[+T] {
    def head: T
    def tail: CBL[T]
  }

  class Human {
    def head: Brain = new Brain
  }

  class Brain {
    override def toString: String = "BRAINS!"
  }

  def f[T](somethingWithAHead: { def head: T }): Unit = println(
    somethingWithAHead.head
  )

  f(new CBL[Int] {
    def head: Int = 42

    def tail: CBL[Int] = this
  })

  f(new Human)

  object HeadEqualizer {
    type Headable[T] = { def head: T }
    def ===[T](a: Headable[T], b: Headable[T]): Boolean = a.head == b.head
  }

  val headHuman = new Human

  val headCBL = new CBL[Brain] {
    def head: Brain = new Brain

    def tail: CBL[Brain] = this
  }

  println(HeadEqualizer.===(headCBL, headHuman))

}
