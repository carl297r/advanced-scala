package lectures.part3concurrency

import scala.collection.mutable
import scala.util.Random

/** @author carl
  * 2021-08-22 16:30
  */
object ThreadCommunication extends App {

  class SimpleContainer {
    private var value: Int = 0

    def isEmpty: Boolean = value == 0
    def set(newValue: Int) = value = newValue
    def get = {
      val result = value
      value = 0
      result
    }
  }

  def nativeProdCons(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer] waiting...")
      while (container.isEmpty) {
        println("[consumer] actively waiting...")
      }

      println("[consumer] I have consumed " + container.get)
    })

    val producer = new Thread(() => {
      println("[producer] computing...")
      Thread.sleep(500)
      val value = 42
      println("[producer] I have produced, after long work, the value " + value)
      container.set(value)
    })

    consumer.start()
    producer.start()
  }

  // nativeProdCons()

  // wait and notify

  def smartProducerCons(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer] Waiting....")
      container.synchronized {
        container.wait()
      }

      println("[consumer] I have consumed " + container.get)
    })

    val producer = new Thread(() => {
      println("[producer] Hard at work...")
      Thread.sleep(2000)
      val value = 42

      container.synchronized {
        println("[producer] I'm producing " + value)
        container.set(value)
        container.notify()
      }
    })

    consumer.start()
    producer.start()
  }

  // smartProducerCons()

  def prodConsLargeBuffer(): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 3

    val consumer = new Thread(() => {
      val random = new Random()

      while (true) {
        buffer.synchronized {
          if (buffer.isEmpty) {
            println("[consumer] buffer empty, waiting...")
            buffer.wait()
          }

          val x = buffer.dequeue()
          println("[consumer] consumed " + x)

          buffer.notify()
        }

        Thread.sleep(random.nextInt(250))
      }

    })

    val producer = new Thread(() => {
      val random = new Random()
      var i = 0

      while (true) {
        buffer.synchronized {
          if (buffer.size == capacity) {
            println("[producer] buffer is full, waiting...")
            buffer.wait()
          }

          println("[producer] producing " + i)
          buffer.enqueue(i)

          buffer.notify()

          i += 1
        }

        Thread.sleep(random.nextInt(500))
      }
    })

    consumer.start()
    producer.start()
  }

//  prodConsLargeBuffer()

  def multiProdCons(nConsumers: Int, nProducers: Int): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 20

    case class consumer(c: Int, buffer: mutable.Queue[Int]) extends Thread {
      override def run(): Unit = {
        val random = new Random()

        while (true) {
          buffer.synchronized {
            while (buffer.isEmpty) {
              println(s"[consumer $c] buffer empty, waiting...")
              buffer.wait()
            }

            val x = buffer.dequeue()
            println(s"[consumer $c] consumed " + x)

            buffer.notify()
          }

          Thread.sleep(random.nextInt(250))
        }
      }
    }

    case class producer(p: Int, buffer: mutable.Queue[Int], capacity: Int)
        extends Thread {
      override def run(): Unit = {
        val random = new Random()
        var i = 0

        while (true) {
          buffer.synchronized {
            while (buffer.size == capacity) {
              println(s"[producer $p] buffer is full, waiting...")
              buffer.wait()
            }

            println(s"[producer $p] producing " + i)
            buffer.enqueue(i)

            buffer.notify()

            i += 1
          }

          Thread.sleep(random.nextInt(500))
        }
      }
    }

    for (i <- 1 to nConsumers) consumer(i, buffer).start()
    for (i <- 1 to nProducers) producer(i, buffer, capacity).start()
  }

  // multiProdCons(3, 6)

  def testNotifyAll(): Unit = {
    val bell = new Object

    (1 to 10).foreach(i =>
      new Thread(() => {
        bell.synchronized {
          println(s"[thread $i] waiting...")
          bell.wait()
          println(s"[thread $i] hooray!")
        }
      }).start()
    )

    new Thread(() => {
      Thread.sleep(2000)
      println("[announcer] Rock n' Roll")
      bell.synchronized {
        bell.notifyAll()
      }
    }).start()
  }

  //testNotifyAll()

  case class Friend(name: String) {
    def bow(other: Friend) = {
      this.synchronized {
        println(s"$this: I am bowing to my friend $other")
        other.rise(this)
        println(s"$this: my friend $other has risen")
      }
    }

    def rise(other: Friend) = {
      this.synchronized {
        println(s"$this: I am rising to my friend $other")
      }
    }

    var side = "right"
    def switchSide(): Unit = {
      if (side == "right") side = "left"
      else side = "right"
    }

    def pass(other: Friend): Unit = {
      while (this.side == other.side) {
        println(s"$this: Oh, but please, $other, feel free to pas...")
        switchSide()
        Thread.sleep(1000)
      }
    }
  }

  val sam = Friend("Sam")
  val pierre = Friend("Pierre")

  // new Thread(() => sam.bow(pierre)).start()
  // new Thread(() => pierre.bow(sam)).start()

  new Thread(() => sam.pass(pierre)).start()
  new Thread(() => pierre.pass(sam)).start()

}
