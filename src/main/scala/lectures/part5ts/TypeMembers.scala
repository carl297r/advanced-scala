package lectures.part5ts

import lectures.part5ts

/** @author carl
  * 2021-08-24 18:12
  */
object TypeMembers extends App {

  class Animal
  class Dog extends Animal
  class Cat extends Animal

  class AnimalCollecton {
    type AnimalType
    type BoundedAnimal <: Animal
    type SuperBoundedAnimal >: Dog <: Animal
    type AnimalC = Cat
  }

  val ac = new AnimalCollecton
  val dog: ac.AnimalType = ???

  // val cat: ac.BoundedAnimal = new Cat

  val pup: ac.SuperBoundedAnimal = new Dog

  val cat: ac.AnimalC = new Cat

  type CatAlias = Cat
  val anotherCat: CatAlias = new Cat

  trait MyList {
    type T
    def add(element: T): MyList
  }

  class NonEmptyList(value: Int) extends MyList {
    type T = Int

    def add(element: Int): MyList = ???
  }

  //.type

  type CatsType = cat.type

  val newCat: CatsType = cat
  // new CatsType

  // LOCKED
  trait MList {
    type A
    def head: A
    def tail: MList
  }

  trait ApplicableToNumbers extends MList {
    type A <: Number
  }

  // NOT OK
  // class CustomList(hd: String, tl: CustomList) extends MList with ApplicableToNumbers {
  //   type A = String
  //   def head = hd
  //   def tail = tl
  // }

  // OK
  class IntList(hd: Int, tl: IntList) extends MList {
    type A = Int
    def head = hd
    def tail = tl
  }

  // NOT OK Either
  // class IntList2(hd: Int, tl: IntList2) extends MList with ApplicableToNumbers {
  //   type A = Int
  //   def head = hd
  //   def tail = tl
  // }

}
