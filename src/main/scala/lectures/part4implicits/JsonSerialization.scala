package lectures.part4implicits

import java.time.LocalDate

/** @author carl
  * 2021-08-24 09:44
  */
object JsonSerialization extends App {

  // Users, posts, feeds

  case class User(name: String, age: Int, email: String)
  case class Post(content: String, createdAt: LocalDate)
  case class Feed(user: User, posts: List[Post])

  sealed trait JsonValue {
    def stringify: String
  }

  final case class JsonString(value: String) extends JsonValue {
    def stringify: String = "\"" + value + "\""
  }
  final case class JsonNumber(value: Int) extends JsonValue {
    def stringify: String = value.toString
  }
  final case class JsonDate(value: LocalDate) extends JsonValue {
    def stringify: String = value.toString
  }
  final case class JsonArray(values: List[JsonValue]) extends JsonValue {
    def stringify: String = values.map(_.stringify).mkString("[", ",", "]")
  }
  final case class JsonObject(values: Map[String, JsonValue])
      extends JsonValue {
    def stringify: String = values
      .map { case (key, value) =>
        "\"" + key + "\":" + value.stringify
      }
      .mkString("{", ",", "}")
  }

  val data = JsonObject(
    Map(
      "user" -> JsonString("Dan"),
      "posts" -> JsonArray(
        List(
          JsonString("Scala Rocks!"),
          JsonNumber(453)
        )
      )
    )
  )

  println(data.stringify)

  //2.1
  trait JsonConverter[T] {
    def convert(value: T): JsonValue
  }

  //3
  implicit class JsonOps[T](value: T) {
    def toJson(implicit converter: JsonConverter[T]): JsonValue =
      converter.convert(value)
  }

  //2.2
  implicit object StringConverter extends JsonConverter[String] {
    def convert(value: String): JsonValue = JsonString(value)
  }

  implicit object NumberConverter extends JsonConverter[Int] {
    def convert(value: Int): JsonValue = JsonNumber(value)
  }

  implicit object LocalDateConverter extends JsonConverter[LocalDate] {
    def convert(value: LocalDate): JsonValue = JsonDate(value)
  }

  implicit object UserConverter extends JsonConverter[User] {
    def convert(user: User): JsonValue = JsonObject(
      Map(
        "name" -> user.name.toJson,
        "age" -> user.age.toJson,
        "email" -> user.email.toJson
      )
    )
  }

  implicit object PostConverter extends JsonConverter[Post] {
    def convert(post: Post): JsonValue = JsonObject(
      Map(
        "content" -> post.content.toJson,
        "createdAt" -> post.createdAt.toJson
      )
    )
  }

  implicit object FeedConverter extends JsonConverter[Feed] {
    def convert(feed: Feed): JsonValue = JsonObject(
      Map(
        "user" -> feed.user.toJson, //TODO
        "posts" -> JsonArray(feed.posts.map(_.toJson)) // TODO
      )
    )
  }

  // use

  val now = LocalDate.now
  val john = User("John", 32, "john@foo.com")
  val feed = Feed(
    john,
    List(
      Post("Hello", now),
      Post("look at puppy", now)
    )
  )

  println(feed.toJson.stringify)

}
