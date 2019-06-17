package au.com.ms.service

import au.com.ms.domain.Condition.{Rain, Snow, Sunny}
import au.com.ms.domain._
import org.scalatest._

class PredictionServiceSpec extends FlatSpec
  with Matchers
  with PredictionService {

  behavior of "PredictionService"

  "genCondition" should "generate condition type" in {

    genCondition(Sunny) shouldBe Rain
    genCondition(Rain) shouldBe Snow
    genCondition(Snow) shouldBe Sunny
  }

  "gen" should "produce value for custom max, min and delta" in {

    gen(30, -30, 30, 0.5, None) shouldBe 29.5
    gen(29.5, -30, 30, 0.5, Some(30)) shouldBe 29
    gen(29, -30, 30, 0.5, Some(29.5)) shouldBe 28.5
    gen(28.5, -30, 30, 0.5, Some(29)) shouldBe 28
    gen(28, -30, 30, 0.5, Some(28.5)) shouldBe 27.5

    gen(-30, -30, 30, 0.5, None) shouldBe -29.5
    gen(-29.5, -30, 30, 0.5, Some(-30)) shouldBe -29
    gen(-29, -30, 30, 0.5, Some(-29.5)) shouldBe -28.5
    gen(-28.5, -30, 30, 0.5, Some(-29)) shouldBe -28
    gen(-28, -30, 30, 0.5, Some(-28.5)) shouldBe -27.5
  }

  "predict" should "produce new WeatherData for one step" in {
    val input = WeatherData(
      "Sydney",
      Coordinate(-33.86, 151.21, 39),
      1560595673042L,
      Sunny,
      Temperature(20),
      1000.1,
      45
    )

    val expected = WeatherData(
      "Sydney",
      Coordinate(-33.86, 151.21, 39),
      1560595674042L,
      Rain,
      Temperature(19.5),
      980.1,
      44)

    predict(input, 1000) shouldBe expected
  }

  "forecast" should "predict weather for 5 steps based on input grid" in {

    val grid =
      Vector(
        WeatherData(
          "Sydney",
          Coordinate(-33.86, 151.21, 39),
          1560595674042L,
          Rain,
          Temperature(12.5),
          1010.3,
          97
        ),
        WeatherData(
          "Melbourne",
          Coordinate(-37.83, 144.98, 7),
          1560595674042L,
          Snow,
          Temperature(-5.3),
          998.4,
          55
        ),
        WeatherData(
          "Adelaide",
          Coordinate(-34.92, 138.62, 48),
          1560595674042L,
          Sunny,
          Temperature(29.4),
          1114,
          12
        )
      )

    val sydney = "Sydney"
    val melbourne = "Melbourne"
    val adelaide = "Adelaide"

    val c1 = Coordinate(-33.86, 151.21, 39.0)
    val c2 = Coordinate(-37.83, 144.98, 7.0)
    val c3 = Coordinate(-34.92, 138.62, 48.0)

    val expected = Vector(
      Vector(
        WeatherData(sydney, c1, 1560595675042L, Snow, Temperature(12.0), 990.3, 96),
        WeatherData(melbourne, c2, 1560595675042L, Sunny, Temperature(-5.8), 978.4, 54),
        WeatherData(adelaide, c3, 1560595675042L, Rain, Temperature(28.9), 1094.0, 11)
      ),
      Vector(
        WeatherData(sydney, c1, 1560595676042L, Sunny, Temperature(11.5), 970.3, 95),
        WeatherData(melbourne, c2, 1560595676042L, Rain, Temperature(-6.3), 958.4, 53),
        WeatherData(adelaide, c3, 1560595676042L, Snow, Temperature(28.4), 1074.0, 10)
      ),
      Vector(
        WeatherData(sydney, c1, 1560595677042L, Rain, Temperature(11), 950.3, 94),
        WeatherData(melbourne, c2, 1560595677042L, Snow, Temperature(-6.8), 938.4, 52),
        WeatherData(adelaide, c3, 1560595677042L, Sunny, Temperature(27.9), 1054.0, 9)
      ),
      Vector(
        WeatherData(sydney, c1, 1560595678042L, Snow, Temperature(10.5), 930.3, 93),
        WeatherData(melbourne, c2, 1560595678042L, Sunny, Temperature(-7.3), 918.4, 51),
        WeatherData(adelaide, c3, 1560595678042L, Rain, Temperature(27.4), 1034.0, 8)
      ),
      Vector(
        WeatherData(sydney, c1, 1560595679042L, Sunny, Temperature(10), 910.3, 92),
        WeatherData(melbourne, c2, 1560595679042L, Rain, Temperature(-7.8), 898.4, 50),
        WeatherData(adelaide, c3, 1560595679042L, Snow, Temperature(26.9), 1014.0, 7)
      )
    )

    val result = forecast(grid, 1000, 1560595679042L).unsafeRunSync()

    result.length shouldBe 5

    result shouldBe expected
  }

  "forecast" should "predict weather for different steps based on input grid" in {

    val grid =
      Vector(
        WeatherData(
          "Sydney",
          Coordinate(-33.86, 151.21, 39),
          1560595674042L,
          Rain,
          Temperature(12.5),
          1010.3,
          97
        ),
        WeatherData(
          "Melbourne",
          Coordinate(-37.83, 144.98, 7),
          1560595674042L,
          Snow,
          Temperature(-5.3),
          998.4,
          55
        ),
        WeatherData(
          "Adelaide",
          Coordinate(-34.92, 138.62, 48),
          1560595674042L,
          Sunny,
          Temperature(39.4),
          1114,
          12
        )
      )

    List(1, 10, 100, 1000).foreach { step =>
      forecast(grid, step, 1560595679042L).unsafeRunSync().length shouldBe 5000 / step
    }
  }

  "forecast" should "fail on empty weather data" in {

    intercept[RuntimeException](forecast(Vector(), 10, 1L)
      .unsafeRunSync()).getMessage shouldBe "Input weather data is empty"
  }

  "forecast" should "fail because end-date < start date" in {

    val data = Vector(WeatherData("City", Coordinate(0, 0, 0), 10L, Sunny, Temperature(0), 0, 0))

    intercept[RuntimeException](forecast(data, 1, 1L)
      .unsafeRunSync()).getMessage shouldBe
      "Weather prediction is available for `end-date` > start date in input data file." +
        "Current parameters: `end-date` = 1, start date: 10"
  }

  "forecast" should "fail because step is too big" in {

    val data = Vector(WeatherData("City", Coordinate(0, 0, 0), 1L, Sunny, Temperature(0), 0, 0))

    intercept[RuntimeException](forecast(data, 100, 10L)
      .unsafeRunSync()).getMessage shouldBe "Calculation step is too big for provided date range"
  }
}
