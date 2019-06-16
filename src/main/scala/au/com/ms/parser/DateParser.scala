package au.com.ms.parser

import java.text.SimpleDateFormat

import scala.util.{Failure, Success, Try}

trait DateParser {

  def parseDate(date: String): Either[Throwable, Long] = {
    val format = "yyyy-MM-dd"
    val sdf = new SimpleDateFormat(format)
    Try(sdf.parse(date)) match {
      case Success(value) => Right(value.toInstant.toEpochMilli)
      case Failure(exception) => Left(exception)
    }

  }
}
