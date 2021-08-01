package lectures.part2afp

/** @author carl
  * 2021-08-06 01:25
  */
object CurriesPAF extends App {
  // curried functions
  val superAdder: Int => Int => Int = x => y => x + y

  val add3 = superAdder(3)
  println(add3(5))
  println(superAdder(3)(5))

  // METHOD!
  def curriedAdder(x: Int)(y: Int): Int = x + y // curried method

  val add4: Int => Int = curriedAdder(4)
  // lifting = ETA-expansion

  // functions != methods (JVM limitation)
  def inc(x: Int): Int = x + 1

  List(1, 2, 3).map(inc)
  List(1, 2, 3).map(x => inc(x)) // ETA-expansion by compiler

  //Partial function application
  // Int => Int as _ tells compiler to do ETA-expansion
  val add5 = curriedAdder(5) _

  // EXERCISE
  val simpleAddFunction = (x: Int, y: Int) => x + y

  def simpleAddMethod(x: Int, y: Int): Int = x + y

  def curriedAddMethod(x: Int)(y: Int): Int = x + y

  // add7: Int => Int = y => 7 + y
  val add702 = (y: Int) => simpleAddFunction(7, y)
  val add700 = simpleAddFunction(7, _)
  val add704 = simpleAddFunction.curried(7)

  val add712 = (y: Int) => simpleAddMethod(7, y)
  val add710 = simpleAddMethod(7, _)

  val add722 = (y: Int) => curriedAddMethod(7)(y)
  val add724 = curriedAddMethod(7) _
  val add720 = curriedAddMethod(7)(_)
  val add725: Int => Int = curriedAddMethod(7)

  // underscores are powerful
  def concatenator(x: String, y: String, z: String) = x + y + z

  def insertName = concatenator("Hello, I'm ", _, ", how are you?")

  println(insertName("Daniel"))

  val fillInTheBlanks = concatenator("Hello, ", _, _)
  println(fillInTheBlanks("Daniel", " Scala is awesome!"))

  // Exercise
  /*
    1. Process List of numbers and return string rep with differnt formats
    %4.2f %8.6f %14.12f
   */
  println("%8.6f".format(Math.PI))
  println(f"${Math.PI}%8.6f")
  println

  val formatter = (s: String) => (d: Double) => s.format(d)
  val format42 = formatter("%4.2f")
  val format86 = formatter("%8.6f")
  val format1412 = formatter("%14.12f")

  List(Math.PI, Math.E, 2.0 / 3.0, 1.3e-12)
    .map(formatter("%14.12f"))
    .foreach(println)
  List(Math.PI, Math.E, 2.0 / 3.0, 1.3e-12).map(format42).foreach(println)
  List(Math.PI, Math.E, 2.0 / 3.0, 1.3e-12).map(format86).foreach(println)
  List(Math.PI, Math.E, 2.0 / 3.0, 1.3e-12).map(format1412).foreach(println)
  /*
    2. Difference bewteen functions and methods
      - functions vs methods
      - paramters: by-name 0-lambda
   */

  def byName(n: => Int) = n + 1

  def byFunction(f: () => Int) = f() + 1

  def method: Int = 42

  def parenMethod(): Int = 42
  /*
    Calling byName and byFunction
    - Int
    - method
    - parenMethod
    - lambda
    - PAF
   */

  byName(23)
  byName(method)
  byName(parenMethod())
  byName(parenMethod) // ok but beware => byName(parenMethod())
  //  byName(() => 12)  // not ok
  byName((() => 12)())
  //  byName(parenMethod _) // not ok
  byName((parenMethod _)())

  //  byFunction(12) // not ok
  //  byFunction(method) // not ok!!!!!! does not do ETA-expansion!
  byFunction(parenMethod)
  byFunction(() => parenMethod()) // above is ETA-expanded to this
  byFunction(() => 12)
  byFunction(parenMethod _) // also works but not required
}
