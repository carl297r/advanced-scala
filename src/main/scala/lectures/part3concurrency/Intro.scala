package lectures.part3concurrency

import java.util.concurrent.Executors

/** @author carl
  * 2021-08-14 16:22
  */
object Intro extends App {

  // JVM threades
  val aThread = new Thread(new Runnable {
    def run(): Unit = println("Running in parallel")
  })

  aThread.start()
  aThread.join()

  val threadHello = new Thread(() => (1 to 5).foreach(_ => println("hello")))
  val threadGoodbye = new Thread(() =>
    (1 to 5).foreach(_ => println("goodbye"))
  )

  threadHello.start()
  threadGoodbye.start()

  val pool = Executors.newWorkStealingPool(10)
  pool.execute(() => println("something in threadpool"))

  pool.execute(() => {
    Thread.sleep(1000)
    println("done after 1 second")
  })

  pool.execute(() => {
    Thread.sleep(1000)
    println("almost done")
    Thread.sleep(1000)
    println("done after 2 seconds")
  })

  pool.shutdown()
  //  pool.execute(() => println("should not appear"))

  //  pool.shutdownNow()
  println(pool.isShutdown)

  var x = 0

  def runInParallel = {
    val thread1 = new Thread(() => x = 1)
    val thread2 = new Thread(() => x = 2)

    thread1.start()
    thread2.start()
    println(x)
  }

  for (_ <- 1 to 10) runInParallel

  class BankAccount(@volatile var amount: Int) {
    override def toString: String = "" + amount
  }

  def buy(account: BankAccount, thing: String, price: Int) = {
    account.amount -= price
    //    println("I've bough" + thing)
    //    println("my account is now " + account)
  }

  for (_ <- 1 to 100) {
    val account = new BankAccount(50000)
    val thread1 = new Thread(() => buy(account, "Shoes", 3000))
    val thread2 = new Thread(() => buy(account, "iphone12", 4000))

    thread1.start()
    thread2.start()
    Thread.sleep(5)
    if (account.amount != 43000) println("Aha: " + account.amount)
    //    println
  }

  def buySafe(account: BankAccount, thing: String, price: Int) = {
    account.synchronized {
      account.amount -= price
      //    println("I've bough" + thing)
      //    println("my account is now " + account)
    }
  }

  def inceptionThreads(max: Int, n: Int = 1): Thread = new Thread(() => {
    if (n < max) {
      val next = inceptionThreads(max, n + 1)
      next.start()
      next.join()
    }
    println(s"hello from thread #$n")
  })

  inceptionThreads(50).start()

  var c = 0
  val threads = (1 to 100).map(_ => new Thread(() => c += 1))
  threads.foreach(_.start())

  var message = ""
  val awesomeThread = new Thread(() => {
    Thread.sleep(1000)
    message = "Scala is awesome"
  })

  message = "Scala sucks"
  awesomeThread.start()
  Thread.sleep(1001)
  println(message)
}
