package au.com.ms.parser

import au.com.ms.domain.Condition._
import au.com.ms.domain.{Coordinate, Temperature, WeatherData}
import cats.effect.IO

import scala.util.{Failure, Success, Try}

trait InputDataParser {

  def parseWeather(fileName: String): IO[Vector[WeatherData]] = {
    val ws = io.Source.fromResource(fileName).getLines.toVector.tail.map { line =>
      val cols = line.split(",").map(_.trim)

      Try(
        WeatherData(
          cols(0),
          Coordinate(cols(1).toDouble, cols(2).toDouble, cols(3).toDouble),
          cols(8).toLong,
          fromString(cols(4)),
          Temperature(cols(5).toDouble),
          cols(6).toDouble,
          cols(7).toInt
        )
      )
    }

    val errors = ws.filter(_.isFailure)

    if (errors.isEmpty) IO.pure(collectValues(ws))
    else IO.raiseError(new RuntimeException(collectErrors(errors)))
  }

  private def collectErrors(vs: Vector[Try[_]]) =
    vs.collect {
      case Failure(err) => err.getMessage
    }.mkString(", ")

  private def collectValues(vs: Vector[Try[WeatherData]]) =
    vs.collect {
      case Success(value) => value
    }

}
