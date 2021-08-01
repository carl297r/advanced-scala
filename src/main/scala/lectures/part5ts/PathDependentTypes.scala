package lectures.part5ts

/** @author carl
  * 2021-08-24 18:56
  */
object PathDependentTypes extends App {

  class Outer {
    class Inner
    object InnerObject
    type InnerType

    def print(i: Inner) = println(i)
    def printGeneral(i: Outer#Inner) = println(i)
  }

  def method: Int = {
    class HelperClass
    type HelperType = String
    2
  }

  // per-instance

  val o = new Outer
  val inner = new o.Inner // o.Inner is a type

  val oo = new Outer
  // val otherInner: oo.Inner = o.Inner

  o.print(inner)
  // oo.print(inner)

  // path-dependent types

  // Outer#Inner
  o.printGeneral(inner)
  oo.printGeneral(inner)

  trait ItemBLike {
    type Key
  }
  trait ItemB[K] extends ItemBLike {
    type Key = K
  }
  trait IntItemB extends ItemB[Int]
  trait StringItemB extends ItemB[String]

  def getB[ItemType <: ItemBLike](key: ItemType#Key): ItemType = ???

  getB[IntItemB](42)
  getB[StringItemB]("home")
  // get[IntItem]("scala") // not ok

  trait Item[K] {
    type Key
  }
  trait IntItem extends Item[Int] {
    type Key = Int
  }
  trait StringItem extends Item[String] {
    type Key = String
  }

  def get[ItemType <: Item[_]](key: ItemType#Key): ItemType = ???

  get[IntItem](42)
  get[StringItem]("home")
  // get[IntItem]("scala") // not ok

  trait ItemA[K]
  trait IntItemA extends ItemA[Int]
  trait StringItemA extends ItemA[String]

  def getA[Key, ItemTypeA <: ItemA[Key]](key: Key): ItemTypeA = ???

  getA[Int, IntItemA](42)
  getA[String, StringItemA]("home")
  // get[IntItem]("scala") // not ok

}
