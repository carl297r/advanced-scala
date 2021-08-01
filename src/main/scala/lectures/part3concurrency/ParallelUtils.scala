package lectures.part3concurrency

import java.util.concurrent.ForkJoinPool
import java.util.concurrent.atomic.AtomicReference
import scala.collection.parallel.ForkJoinTaskSupport
import scala.collection.parallel.immutable.ParVector

/** @author carl
  * 2021-08-22 20:02
  */
object ParallelUtils extends App {

  val parList = List(1, 2, 3).par

  val aParVector = ParVector[Int](1, 2, 3)

  def measure[T](operation: => T): Long = {
    val time = System.currentTimeMillis()
    operation
    System.currentTimeMillis() - time
  }

  val list = (1 to 10000).toList
  val serialTime = measure {
    list.map(_ + 2)
  }
  println("serial time: " + serialTime)

  val parallelTime = measure {
    list.par.map(_ + 2)
  }
  println("parallel time: " + parallelTime)

  println(List(1, 2, 3).reduce(_ - _))
  println(List(1, 2, 3).par.reduce(_ - _))

  var sum = 0
  List(1, 2, 3, 4, 5, 6, 7, 8, 9).par.foreach(sum += _)
  println(sum)

  aParVector.tasksupport = new ForkJoinTaskSupport(new ForkJoinPool(2))

  val atomic = new AtomicReference[Int](2)

  val currentValue = atomic.get()
  atomic.set(4)
  atomic.getAndSet(5)
  atomic.compareAndSet(38, 56)
  atomic.updateAndGet(_ + 1)
  atomic.getAndUpdate(_ + 1)
  atomic.accumulateAndGet(12, _ + _)
  atomic.getAndAccumulate(12, _ + _)

}
