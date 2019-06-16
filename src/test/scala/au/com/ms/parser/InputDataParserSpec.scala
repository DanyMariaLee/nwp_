package au.com.ms.parser

import au.com.ms.domain.Condition.{Rain, Snow, Sunny}
import au.com.ms.domain._
import cats.effect.IO
import org.scalatest._

class InputDataParserSpec extends FlatSpec with Matchers with InputDataParser {

  behavior of "InputDataParser"

  "parseWeather" should "produce WeatherInput data" in {

    val expected = Seq(
      WeatherData("Melbourne", Coordinate(-37.814, 144.96332, 7.0),
        1560595673042L, Rain, Temperature(12.5), 1010.3, 97
      ),
      WeatherData("Amsterdam", Coordinate(52.379189, 4.899431, 13.0),
        1560595677042L, Sunny, Temperature(30.5), 998.4, 96
      ),
      WeatherData("Moscow", Coordinate(55.98, 37.18, 220.0),
        1560595673042L, Snow, Temperature(-20.0), 1114.1, 83
      ),
    )

    parseWeather("input.csv") shouldBe IO.pure(expected)
  }

  "parseWeather" should "fail with wrong condition data" in {

    val errorMessage = "Unknown condition: FOO, Unknown condition: BAR, Unknown condition: FOOBAR"

    intercept[RuntimeException](
      parseWeather("wrongcondition.csv").unsafeRunSync()
    ).getMessage shouldBe errorMessage

  }

  "parseWeather" should "fail with wrong data format in column" in {

    val errorMessage = """For input string: ""dddd"", For input string: ""kkk"""""

    intercept[RuntimeException](
      parseWeather("wrongformat.csv").unsafeRunSync()
    ).getMessage shouldBe errorMessage
  }
}
