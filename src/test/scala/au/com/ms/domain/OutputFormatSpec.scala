package au.com.ms.domain

import au.com.ms.domain.Condition.{Rain, Snow}
import org.scalatest._

class OutputFormatSpec extends FlatSpec with Matchers {

  behavior of "WeatherData string formatter function"

  "show on WeatherData" should "format data in string with separators" in {

    val p1 = WeatherData(
      "Sydney",
      Coordinate(-33.86, 151.21, 39),
      1560666341000L,
      Rain,
      Temperature(12.5),
      1004.3,
      97
    )

    val p2 = WeatherData(
      "Melbourne",
      Coordinate(-37.83, 144.98, 7),
      1570686351000L,
      Snow,
      Temperature(-5.3),
      998.4,
      55
    )

    p1.show should be(
      "Sydney|-33.86,151.21,39.0|2019-06-16 16:25:41|Rain|+12.5|1004.3|97"
    )

    p2.show should be(
      "Melbourne|-37.83,144.98,7.0|2019-10-10 16:45:51|Snow|-5.3|998.4|55"
    )
  }

}
