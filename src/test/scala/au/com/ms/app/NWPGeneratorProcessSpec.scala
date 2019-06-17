package au.com.ms.app

import au.com.ms.domain.Condition.Sunny
import au.com.ms.domain.{Coordinate, Temperature, WeatherData}
import cats.effect.IO
import org.scalatest.{FlatSpec, Matchers}

class NWPGeneratorProcessSpec extends FlatSpec with Matchers {

  behavior of "NWPGeneratorProcess"

  "run" should "exit without exceptions" in {

    val process = buildProcess()
    process.run.unsafeRunSync() shouldBe()
  }

  "run" should "fail because parseConfig failed" in {

    val process = buildProcess(parseConfigResponse = IO.raiseError(new Exception("parseConfig failed")))
    intercept[Exception](process.run.unsafeRunSync()).getMessage shouldBe "parseConfig failed"
  }

  "run" should "fail because parseDate failed" in {

    val process = buildProcess(parseDateResponse = Left(new Exception("parseDate failed")))
    intercept[Exception](process.run.unsafeRunSync()).getMessage shouldBe "parseDate failed"
  }

  "run" should "fail because parseWeather failed" in {

    val process = buildProcess(parseWeatherResponse = IO.raiseError(new Exception("parseWeather failed")))
    intercept[Exception](process.run.unsafeRunSync()).getMessage shouldBe "parseWeather failed"
  }

  "run" should "fail because forecast failed" in {

    val process = buildProcess(forecastResponse = IO.raiseError(new Exception("forecast failed")))
    intercept[Exception](process.run.unsafeRunSync()).getMessage shouldBe "forecast failed"
  }

  "run" should "fail because write failed" in {

    val process = buildProcess(writeResponse = IO.raiseError(new Exception("write failed")))
    intercept[Exception](process.run.unsafeRunSync()).getMessage shouldBe "write failed"
  }

  val data = Vector(Vector(WeatherData("Amsterdam", Coordinate(52.379189, 4.899431, 13.0),
    1560595677042L, Sunny, Temperature(30.5), 998.4, 96
  )))

  def buildProcess(forecastResponse: IO[Vector[Vector[WeatherData]]] = IO(data),
                   parseConfigResponse: IO[NWPConfig] = IO(NWPConfig("", 1L, false, "", "")),
                   parseDateResponse: Either[Throwable, Long] = Right(1L),
                   parseWeatherResponse: IO[Vector[WeatherData]] = IO(data.head),
                   writeResponse: IO[Unit] = IO(())
                  ) = new NWPGeneratorProcess {

    override def forecast(weather: Vector[WeatherData], step: Long, endDate: Long): IO[Vector[Vector[WeatherData]]] = forecastResponse

    override def parseConfig: IO[NWPConfig] = parseConfigResponse

    override def parseDate(date: String): Either[Throwable, Long] = parseDateResponse

    override def parseWeather(fileName: String): IO[Vector[WeatherData]] = parseWeatherResponse

    override def write(fileName: String, data: Vector[Vector[WeatherData]], addHeader: Boolean): IO[Unit] = writeResponse
  }

}
