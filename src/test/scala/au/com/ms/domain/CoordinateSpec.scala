package au.com.ms.domain

import org.scalatest.{FlatSpec, Matchers}

class CoordinateSpec extends FlatSpec with Matchers {

  behavior of "Coordinate"

  "toString" should "format coordinates" in {

    Coordinate(10.11,123.11,9).toString shouldBe "10.11,123.11,9.0"
    Coordinate(10,123,9).toString shouldBe "10.0,123.0,9.0"
  }
}
