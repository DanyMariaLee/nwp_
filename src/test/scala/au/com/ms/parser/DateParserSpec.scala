package au.com.ms.parser

import org.scalatest.{FlatSpec, Matchers}

class DateParserSpec extends FlatSpec with Matchers with DateParser {

  behavior of "DateParser"

  "parseDate" should "format the date correctly" in {

    parseDate("2019-06-17") shouldBe Right(1560693600000L)
  }

  "parseDate" should "fail to format the date correctly" in {

    parseDate("2019/06/17") match {
      case Left(_) => ()
      case _ => fail("This test should fail")
    }

    parseDate("20190617") match {
      case Left(_) => ()
      case _ => fail("This test should fail")
    }
  }
}
