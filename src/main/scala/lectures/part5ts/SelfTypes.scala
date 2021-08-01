package lectures.part5ts

/** @author carl
  * 2021-08-25 11:10
  */
object SelfTypes extends App {

  // Requires a type to be mixed in

  trait Instrumentalist {
    def play(): Unit
  }

  trait Singer {
    self: Instrumentalist => // self typpe

    def sing(): Unit
  }

  class LeadSinger extends Singer with Instrumentalist {
    def sing(): Unit = ???
    def play(): Unit = ???
  }

  // class Vocalist extends Singer {
  //   def sing(): Unit = ???
  // }

  val jamesHetfield = new Singer with Instrumentalist {
    def sing(): Unit = ???
    def play(): Unit = ???
  }

  class Guitarist extends Instrumentalist {
    def play(): Unit = println("(guitar solo)")
  }

  val ericClaption = new Guitarist with Singer {
    def sing(): Unit = ???
  }

  // vs inheritance

  class A
  class B extends A // B is an A

  trait T
  trait S { self: T => } // S requires a T

  // cake pattern => "dependency injection"

  // DI
  class Component {
    // api
  }
  class ComponentA extends Component
  class ComponentB extends Component
  class DependentComponent(val component: Component)

  // Cake Pattern
  trait ScalaComponent {
    // api
    def action(x: Int): String
  }
  trait ScalaDependentComponent { self: ScalaComponent =>
    def dependentAction(x: Int): String = action(x) + " this rocks!"
  }
  trait ScalaApplication { self: ScalaDependentComponent => }

  // Layer 1
  trait Picture extends ScalaComponent
  trait Stats extends ScalaComponent

  // Layer 2
  trait Profile extends ScalaDependentComponent with Picture
  trait Analytics extends ScalaDependentComponent with Stats

  // Layer 3
  trait AnalyticsApp extends ScalaApplication with Analytics

  // cyclical dependencies
  // class X extends Y
  // class Y extends X

  trait X { self: Y => }
  trait Y { self: X => }

}
