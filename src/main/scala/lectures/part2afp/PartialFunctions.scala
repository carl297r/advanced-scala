package lectures.part2afp

/** @author carl
  * 2021-08-04 01:04
  */
object PartialFunctions extends App {
  val aFunction = (x: Int) => x + 1 // Function1 [Int, Int]

  val aFuzzyFuncion = (x: Int) =>
    if (x == 1) 42
    else if (x == 2) 56
    else if (x == 5) 999
    else throw new FunctionNotApplicableException

  class FunctionNotApplicableException extends RuntimeException

  val aNicerFuzzyFunction = (x: Int) =>
    x match {
      case 1 => 42
      case 2 => 56
      case 5 => 999
    }
  // (1, 2, 5) => Int

  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  } // Partial Function

  println(aPartialFunction(2))
  //  println(aPartialFunction(57272))

  // pf utilities
  println(aPartialFunction.isDefinedAt(57))

  // lift
  val lifted = aPartialFunction.lift // Int => Option(Int)
  println(lifted(2))
  println(lifted(98))

  val pfChain = aPartialFunction.orElse[Int, Int] { case 45 => 67 }

  println(pfChain(2))
  println(pfChain(45))

  // pf extend normal functions

  val aTotalFunction: Int => Int = { case 1 =>
    99
  }

  // HDFs accept partial functions as well
  val aMappedList = List(1, 2, 5).map {
    case 1 => 42
    case 2 => 78
    case 5 => 1000
  }
  println(aMappedList)

  /*
    Note: PF can ony have ONE parameter type
   */

  /** Exercises
    *
    * 1 - construct a PF instance yourself (anonymous class)
    * 2 - dump chatbot as a PF
    */

  val chatBot = new PartialFunction[String, String] {
    def isDefinedAt(x: String): Boolean = x match {
      case "hello" | "fine" | "good" => true
      case _                         => false
    }

    def apply(v1: String): String = v1 match {
      case "hello" => "hello, how are you?"
      case "fine"  => "just fine?"
      case "good"  => "home come?"
    }
  }

  //  scala.io.Source.stdin.getLines().foreach(line =>
  //  println("you said: " + line))
  scala.io.Source.stdin.getLines().map(chatBot).foreach(println)

}
