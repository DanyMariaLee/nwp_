package au.com.ms.domain

sealed trait Condition

object Condition {
  val values = List(Rain, Sunny, Snow)

  def fromString(s: String): Condition =
    values.find(_.toString.equalsIgnoreCase(s))
      .getOrElse(throw new NoSuchElementException(s"Unknown condition: $s"))

  case object Rain extends Condition

  case object Snow extends Condition

  case object Sunny extends Condition

}