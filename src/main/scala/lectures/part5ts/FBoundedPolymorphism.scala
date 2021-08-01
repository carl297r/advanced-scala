package lectures.part5ts

/** @author carl
  * 2021-08-25 11:30
  */
object FBoundedPolymorphism extends App {

  // trait Animal {
  //   def breed: List[Animal]
  // }
  //
  // trait Cat extends Animal {
  //   def breed: List[Animal] = ??? // List[Cat]
  // }
  //
  // trait Dog extends Animal {
  //   def breed: List[Animal] = ??? // List[Dog]
  // }

  // Solution 1 - naive
  // trait Animal {
  //   def breed: List[Animal]
  // }
  //
  // trait Cat extends Animal {
  //   def breed: List[Cat] = ??? // List[Cat]
  // }
  //
  // trait Dog extends Animal {
  //   def breed: List[Cat] = ??? // List[Dog]
  // }

  // Solution 2 - FBP
  // Recursive type - F-Bounded Polymorphism
  // trait Animal[A <: Animal[A]] {
  //   def breed: List[A]
  // }
  //
  // trait Cat extends Animal[Cat] {
  //   def breed: List[Cat] = ??? // List[Cat]
  // }
  //
  // trait Dog extends Animal[Dog] {
  //   def breed: List[Dog] = ??? // List[Dog]
  // }
  //
  // trait Entity[E <: Entity[E]] // ORM
  // class Person extends Comparable[Person] {
  //   def compareTo(o: Person): Int = ???
  // }
  //
  // // Wrong still
  // class Crocodile extends Animal[Dog] {
  //   def breed: List[Dog] = ???
  // }

  // Solution 3 - FBP + self-types
  // trait Animal[A <: Animal[A]] {
  //   self: A =>
  //   def breed: List[A]
  // }
  //
  // trait Cat extends Animal[Cat] {
  //   def breed: List[Cat] = ??? // List[Cat]
  // }
  //
  // trait Dog extends Animal[Dog] {
  //   def breed: List[Dog] = ??? // List[Dog]
  // }

  // Wrong still
  // class Crocodile extends Animal[Dog] {
  //   def breed: List[Dog] = ???
  // }

  // trait Fish extends Animal[Fish]
  // class Shark extends Fish {
  //   def breed: List[Fish] = List(new Cod) // Wrong
  // }
  // class Cod extends Fish {
  //   def breed: List[Fish] = ???
  // }

  // Solution

  // Solution 4 - type classes!
  // trait Animal
  // // 1 - Type class
  // trait CanBreed[A] {
  //   def breed(a: A): List[A]
  // }
  //
  // // 2 - Type class instance for Dog
  // class Dog extends Animal
  // object Dog {
  //   implicit object DogsCanBreed extends CanBreed[Dog] {
  //     def breed(a: Dog): List[Dog] = List(new Dog)
  //   }
  // }
  //
  // // 3 - Implicit Conversion to add breed method to anything with a CanBreed type class
  // implicit class CanBreedOps[A](animal: A) {
  //   def breed(implicit canBreed: CanBreed[A]): List[A] = canBreed.breed(animal)
  // }
  //
  // val dog = new Dog
  // dog.breed
  // // Becomes
  // new CanBreedOps[Dog](dog).breed(Dog.DogsCanBreed)
  //
  // class Cat extends Animal
  // object Cat {
  //   implicit object CatsCanBreed extends CanBreed[Dog] {
  //     def breed(a: Dog): List[Dog] = List(new Dog)
  //   }
  // }
  //
  // val cat = new Cat
  // cat.breed

  // Soultion 5
  // 1 - type class
  trait Animal[A] { //pure type class
    def breed(a: A): List[A]
  }

  // 2 - implementation for Dog
  class Dog
  object Dog {
    implicit object DogAnimal extends Animal[Dog] {
      def breed(a: Dog): List[Dog] = List(new Dog)
    }
  }

  class Cat
  object Cat {
    implicit object CatAnimal extends Animal[Dog] {
      def breed(a: Dog): List[Dog] = List(new Dog)
    }
  }

  // 3 - Implicit conversion to add method that uses type classes

  implicit class AnimalOps[A](animal: A) {
    def breed(implicit animalTypeClassInstance: Animal[A]): List[A] =
      animalTypeClassInstance.breed(animal)
  }

  val dog = new Dog
  dog.breed

  // will not compile
  // val cat = new Cat
  // cat.breed

}
