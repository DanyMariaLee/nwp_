package au.com.ms.app

import java.io.File

import cats.effect.IO
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

class NWPGeneratorProcessWorkflowSpec extends FlatSpec
  with Matchers with BeforeAndAfter {

  behavior of "NWPGeneratorProcess"

  val outputFileName = "output_2.csv"
  val outputFile = new File(outputFileName)

  before {
    if (outputFile.exists()) outputFile.delete()
  }

  after(outputFile.delete())

  "run" should "read weather from file, calculate predictions and " +
    "save into output file" in {

    val process = new NWPGeneratorProcess {
      override def parseConfig: IO[NWPConfig] =
        IO(NWPConfig("2019-06-16", 10000L, false, "input_2.csv", "output_2.csv"))
    }

    process.run.unsafeRunSync()

    io.Source.fromFile(outputFileName).getLines.length shouldBe 2305
  }

}
