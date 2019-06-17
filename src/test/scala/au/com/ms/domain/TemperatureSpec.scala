package au.com.ms.domain

import org.scalatest.{FlatSpec, Matchers}

class TemperatureSpec extends FlatSpec with Matchers {

  behavior of "Temperature"
/*
  "sign" should "produce the sign for value" in {

    Temperature(10).sign shouldBe "+"
    Temperature(0).sign shouldBe ""
    Temperature(-20.3).sign shouldBe "-"
  }*/

  "toString" should "add temperature sign when formatting a string" in {

    Temperature(10).toString shouldBe "+10"
    Temperature(0).toString shouldBe "0"
    Temperature(-20.3).toString shouldBe "-20.3"
  }

}
