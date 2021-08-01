package lectures.part5ts

/** @author carl
  * 2021-08-24 16:11
  */
object Variance extends App {

  trait Animal
  class Dog extends Animal
  class Cat extends Animal
  class Crocodile extends Animal

  // what is variance?
  // "inheritance" - tye substitution of generics

  class Cage[T]
  // yes - coveriance
  class CCage[+T]
  val ccage: CCage[Animal] = new CCage[Cat]

  // no - invariance
  class ICage[T]
  // val icage: ICage[Animal] = new ICage[Cat]
  // val x: Int = 'hello'

  // hell no - opposite - contravariance
  class XCage[-T]
  val XCage: XCage[Cat] = new XCage[Animal]

  class InvariantCage[T](val animal: T) // invariant
  new InvariantCage(new Animal {})
  new InvariantCage(new Dog)
  new InvariantCage(new Cat)

  // covariant position
  class CovariantCage[+T](val animal: T) // covariant position
  new CovariantCage(new Animal {})
  new CovariantCage(new Dog)
  new CovariantCage(new Cat)

  // class ContravariantCage[-T](val animal: T) // covariant position
  /*
    val catCage: XCage[Cat] = new XCage[Animal](new Crocodile)
   */

  // class CovariantVariableCage[+T](var animal: T) // contravarient position
  /*
    val cCage: CCage[Animal] = new CCage[Cat](new Cat)
    cCage.animal = new Crocodile
   */

  // class ContravariantVariableCage[-T](var animal: T) // also covariant position
  /*
    val catCage: XCage[Cat] = new XCage[Animal](new Crocodile)
   */
  class InvariantVariableCage[T](var animal: T) //okR

  // trait AnotherCovariantCage[+T] {
  //   def addAnimal(animal: T)
  // }
  /*
      val ccage: CCage[Animal] = new CCage[Dog]
      cccage.add(new Cat)
   */
  class AnotherContravariantCage[-T] {
    def addAnimal(animal: T) = true
  }
  val acc: AnotherContravariantCage[Cat] = new AnotherContravariantCage[Animal]
  // acc.addAnimal(new Dog)
  acc.addAnimal(new Cat)

  class Kitty extends Cat
  acc.addAnimal(new Kitty)

  class MyList[+A] {
    def add[B >: A](element: B): MyList[B] = new MyList[B]
  }

  val emptyList = new MyList[Kitty]
  val animals = emptyList.add(new Kitty)
  val moreAnimals = animals.add(new Cat)
  val evenMoreAnimals = animals.add(new Dog)

  // METHOD ARGUMENTS ARE IN CONTRAVARIANT POSITION

  // return types

  class PetShop[-T] {
    // def get(isItAPuppy: Boolean): T  // METHOD RETURN TYPES ARE IN COVARIANT POSITION
    /*
      val catShop = new PatShop[Animal] {
        def get(isItAPuppy: Boolean): Animal = new Cat
      }

      val dogShop = new PetShop: PetShop[Dog] = catShop
      dogShop.get(true) /// EVIL CATS
     */

    def get[S <: T](isItAPuppy: Boolean, defaultAnimal: S): S = defaultAnimal

  }

  val shop: PetShop[Dog] = new PetShop[Animal]

  // val evilCat = shop.get(true, new Cat)

  class TerraNova extends Dog
  val bigFurry = shop.get(true, new TerraNova)

  /*
    Big Rule
    - method arguments are in Contravariant position
    - return types are in Covariant position
   */

  class Vehicle
  class Bike extends Vehicle
  class Car extends Vehicle

  class IParking[T](things: List[T]) {
    def park(vehicle: T): IParking[T] = ???
    def impound(vehicles: List[T]): IParking[T] = ???
    def checkVehicles(conditions: String): List[T] = ???

    def flatMap[S](f: T => IParking[S]): IParking[S] = ???
  }

  class CParking[+T](things: List[T]) {
    def park[S >: T](vehicle: S): CParking[S] = ???
    def impound[S >: T](vehicles: List[S]): CParking[S] = ???
    def checkVehicles(conditions: String): List[T] = ???

    def flatMap[S](f: T => CParking[S]): CParking[S] = ???
  }

  class XParking[-T](things: List[T]) {
    def park(vehicle: T): XParking[T] = ???
    def impound(vehicles: List[T]): XParking[T] = ???
    def checkVehicles[S <: T](conditions: String): List[S] = ???

    def flatMap[S, U <: T](f: U => XParking[S]): XParking[S] = ???
  }

  class IList[T]

  class IIParking[T](things: IList[T]) {
    def park(vehicle: T): IIParking[T] = ???
    def impound(vehicles: IList[T]): IIParking[T] = ???
    def checkVehicles(conditions: String): IList[T] = ???
  }

  class ICParking[+T](things: IList[T]) {
    def park[S >: T](vehicle: S): ICParking[S] = ???
    def impound[S >: T](vehicles: IList[S]): ICParking[S] = ???
    def checkVehicles[S >: T](conditions: String): IList[S] = ???
  }

  class IXParking[-T](things: IList[T]) {
    def park(vehicle: T): IXParking[T] = ???
    def impound[S <: T](vehicles: IList[S]): IXParking[S] = ???
    def checkVehicles[S <: T](conditions: String): IList[S] = ???
  }

  /*
    Rule of thumb
    - use covariance = COLLECTION of THINGS
    - use contravariance = COLLECTION of ACTIONS
   */

}
