package lectures.part4implicits

/** @author carl
  * 2021-08-23 06:44
  */
object OrganizingImplicits extends App {

  implicit val reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  // implicit val normalOrdering: Ordering[Int] = Ordering.fromLessThan(_ < _)

  println(List(1, 4, 5, 3, 2).sorted)

  /*
    Implicits:
      - val/var
      - object
      - accessor methods = defs with no parentheses
   */

  case class Person(name: String, age: Int)

  val persons = List(
    Person("Steve", 30),
    Person("Amy", 22),
    Person("John", 66)
  )

  object Person {
    // implicit val alphabeticOrdering: Ordering[Person] =
    //   Ordering.fromLessThan(_.name < _.name)

    implicit val alphabeticOrdering: Ordering[Person] =
      Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
  }

  implicit val ageOrdering: Ordering[Person] =
    Ordering.fromLessThan(_.age < _.age)

  println(persons.sorted)

  /*
   * Implicit scope
   *  - normal scope = Local Scope
   *  - imported scope
   *  - companion objects of all types involved in method signature
   *    - List
   *    - Ordering
   *    - all the types involved = A or any supertypes
   * */

  case class Purchase(nUnits: Int, unitPrice: Double)
  object Purchase {
    implicit val priceOrdering: Ordering[Purchase] =
      Ordering.fromLessThan((x, y) =>
        x.unitPrice * x.nUnits < y.unitPrice * y.nUnits
      )
  }

  object CountOrdering {
    implicit val countOrdering: Ordering[Purchase] =
      Ordering.fromLessThan(_.nUnits < _.nUnits)
  }

  object unitPriceOrdering {
    implicit val priceOrdering: Ordering[Purchase] =
      Ordering.fromLessThan(_.unitPrice < _.unitPrice)
  }

  /*
   * Best practices
   * 1. If sinngle possible value for it
   *    And you can edit the code for the type
   *    Then define the implicit in the companion
   *
   * 2. If there are many possible values
   *    But a single good value
   *    And you can edit the code for the type
   *    THen define the good implicit in the companion
   *
   * 3.
   *
   *
   * */
}
