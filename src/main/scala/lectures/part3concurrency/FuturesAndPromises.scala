package lectures.part3concurrency

import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Failure, Random, Success, Try}

/** @author carl
  * 2021-08-22 18:06
  */
object FuturesAndPromises extends App {

  def calculateMeaningOfLife: Int = {
    Thread.sleep(2000)
    42
  }

  val aFuture = Future {
    calculateMeaningOfLife
  }

  println(aFuture.value)

  println("Waiting on the future")
  aFuture.onComplete {
    case Success(meaningOfLife) =>
      println(s"the meaning of life is $meaningOfLife")
    case Failure(e) =>
      println(s"I have failed with $e")
  }

  Thread.sleep(3000)

  case class Profile(id: String, name: String) {
    def poke(anotherProfile: Profile) = {
      println(s"${this.name} poking ${anotherProfile.name}")
    }
  }

  object SocialNetwork {
    val names = Map(
      "fb.id.1-zuck" -> "Mark",
      "fb.id.2-bill" -> "Bill",
      "fb.id.0-dummy" -> "Dummy"
    )

    val friends = Map("fb.id.1-zuck" -> "fb.id.2-bill")

    val random = new Random()

    def fetchProfile(id: String): Future[Profile] = Future {
      Thread.sleep(random.nextInt(300))
      Profile(id, names(id))
    }
    def fetchBestFriend(profile: Profile): Future[Profile] = Future {
      Thread.sleep(random.nextInt(400))
      val bfId = friends(profile.id)
      Profile(bfId, names(bfId))
    }
  }

  val mark = SocialNetwork.fetchProfile("fb.id.1-zuck")
  mark.onComplete {
    case Success(markProfile) => {
      val bill = SocialNetwork.fetchBestFriend(markProfile)
      bill.onComplete {
        case Success(billProfile) => markProfile.poke(billProfile)
        case Failure(e)           => e.printStackTrace()
      }
    }
    case Failure(ex) => ex.printStackTrace()
  }

  Thread.sleep(1000)

  val nameOnTheWall = mark.map(profile => profile.name)
  val marksBestFriend =
    mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))
  val zucksBestFriendRestricted =
    marksBestFriend.filter(profile => profile.name.startsWith("Z"))

  for {
    mark <- SocialNetwork.fetchProfile("fb.id.1-zuck")
    bill <- SocialNetwork.fetchBestFriend(mark)
  } mark.poke(bill)

  Thread.sleep(1000)

  SocialNetwork.fetchProfile("unknown").recover { case e: Throwable =>
    Profile("fb.id.0-dummy", "Forever alone")
  }

  val aFetchedProfileNoMatterWhat =
    SocialNetwork.fetchProfile("unknown").recoverWith { case e: Throwable =>
      SocialNetwork.fetchProfile("fb.id.0-dummy")
    }

  SocialNetwork
    .fetchProfile("unknown")
    .fallbackTo(SocialNetwork.fetchProfile("fb.id.0-dummy"))

  case class User(name: String)
  case class Transaction(
      sender: String,
      receiver: String,
      amount: Double,
      status: String
  )

  object BankingApp {
    val name = "Rock the JVM banking"

    def fetchUser(name: String): Future[User] = Future {
      Thread.sleep(500)
      User(name)
    }

    def createTransaction(
        user: User,
        merchantName: String,
        amount: Double
    ): Future[Transaction] = Future {
      Thread.sleep(1000)
      Transaction(user.name, merchantName, amount, "OK")
    }

    def purchase(
        username: String,
        item: String,
        merchantName: String,
        cost: Double
    ): String = {
      val transactionStatusFuture = for {
        user <- fetchUser(username)
        transaction <- createTransaction(user, merchantName, cost)
      } yield transaction.status

      Await.result(transactionStatusFuture, 2.seconds)

    }

  }

  println(BankingApp.purchase("Dan", "iPhone", "rick the jvm store", 3000))

  // promises

  val promise = Promise[Int] // * controller* over a future
  val future = promise.future

  future.onComplete { case Success(value) =>
    println("[consumer] I've received " + value)
  }

  val producer = new Thread(() => {
    println("[producer] crunching numbers...")
    Thread.sleep(500)
    promise.success(42)
    println("[producer] done")
  })

  producer.start()

  Thread.sleep(1000)

  val foo = Future(42)

  def inSequence[T, U](fa: Future[T], fb: Future[U]): Future[U] =
    fa.flatMap(_ => fb)

  def first[T](fa: Future[T], fb: Future[T]): Future[T] = {
    val promise = Promise[T]
    fa.onComplete(promise.tryComplete)
    fb.onComplete(promise.tryComplete)
    promise.future
  }

  first(Future(2), Future(3)).foreach(println)

  Thread.sleep(1000)

}
