package au.com.ms.parser

import au.com.ms.app.NWPConfig
import cats.effect.IO
import org.scalatest.{FlatSpec, Matchers}

class ConfigParserSpec extends FlatSpec with Matchers with ConfigParser {

  behavior of "ConfigParser"

  "parseConfig" should "parse configuration correctly" in {

    val expected = NWPConfig("2019-06-16", 10000L, true, "input.csv", "output.csv")

    parseConfig shouldBe IO.pure(expected)
  }

}
