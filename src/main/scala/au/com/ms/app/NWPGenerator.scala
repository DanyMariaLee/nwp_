package au.com.ms.app

import au.com.ms.domain.WeatherData
import au.com.ms.fileutils.FileWriter
import au.com.ms.parser.{ConfigParser, DateParser, InputDataParser}
import au.com.ms.service.PredictionService
import cats.effect.IO
import pureconfig.generic.auto._

object NWPGenerator extends App with ConfigParser with InputDataParser
  with PredictionService with DateParser with FileWriter {

  def run = for {
    config <- parseConfig
    endDate <- IO.fromEither(parseDate(config.endDate))
    weather <- parseWeather(config.inputFile)
    generated <- forecast(weather, config.step, endDate)
    _ <- printOut(config, generated)
    _ <- write(config.outputFile, generated)
  } yield ()

  run.unsafeRunSync()

  private def printOut(config: NWPConfig, data: Vector[Vector[WeatherData]]): IO[Unit] =
    if (config.printOut) IO(data.foreach(_.foreach(w => println(w.show))))
    else IO.pure(())

}
