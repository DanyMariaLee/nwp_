package au.com.ms.fileutils

import java.io.File

import au.com.ms.domain.Condition.{Rain, Snow, Sunny}
import au.com.ms.domain.{Coordinate, Temperature, WeatherData}
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

class FileWriterSpec extends FlatSpec with Matchers with BeforeAndAfter {

  behavior of "FileWriter"

  val path = this.getClass.getResource("/").getPath
  val dummyFilePath = path + "dummy.csv"
  val dummyFile = new File(dummyFilePath)

  before {
    if (dummyFile.exists()) dummyFile.delete()
  }

  after(dummyFile.delete())

  "write" should "write to file successfully without headers" in {
    val writer = new FileWriter {}

    val data = Vector(
      Vector(
        WeatherData("Melbourne", Coordinate(-37.814, 144.96332, 7.0),
          1560595673042L, Rain, Temperature(12.5), 1010.3, 97
        ),
        WeatherData("Amsterdam", Coordinate(52.379189, 4.899431, 13.0),
          1560595677042L, Sunny, Temperature(30.5), 998.4, 96
        )
      ),
      Vector(
        WeatherData("Melbourne", Coordinate(-37.814, 144.96332, 7.0),
          1560595773042L, Sunny, Temperature(15.5), 1010.3, 97
        ),
        WeatherData("Amsterdam", Coordinate(52.379189, 4.899431, 13.0),
          1560595773042L, Snow, Temperature(29.5), 998.4, 96
        )
      )
    )

    writer.write(dummyFilePath, data, false).unsafeRunSync()
    val result = io.Source.fromFile(dummyFilePath).getLines.toList.mkString("", "\n", "\n")

    val expected =
      """Melbourne|-37.814,144.96332,7.0|2019-06-15 20:47:53|Rain|+12.5|1010.3|97
        |Amsterdam|52.379189,4.899431,13.0|2019-06-15 20:47:57|Sunny|+30.5|998.4|96
        |Melbourne|-37.814,144.96332,7.0|2019-06-15 20:49:33|Sunny|+15.5|1010.3|97
        |Amsterdam|52.379189,4.899431,13.0|2019-06-15 20:49:33|Snow|+29.5|998.4|96
        |""".stripMargin

    result shouldBe expected
  }

  "write" should "write to file successfully with headers" in {
    val writer = new FileWriter {}

    val data = Vector(
      Vector(
        WeatherData("Melbourne", Coordinate(-37.814, 144.96332, 7.0),
          1560595673042L, Rain, Temperature(12.5), 1010.3, 97
        ),
        WeatherData("Amsterdam", Coordinate(52.379189, 4.899431, 13.0),
          1560595677042L, Sunny, Temperature(30.5), 998.4, 96
        )
      ),
      Vector(
        WeatherData("Melbourne", Coordinate(-37.814, 144.96332, 7.0),
          1560595773042L, Sunny, Temperature(15.5), 1010.3, 97
        ),
        WeatherData("Amsterdam", Coordinate(52.379189, 4.899431, 13.0),
          1560595773042L, Snow, Temperature(29.5), 998.4, 96
        )
      )
    )

    writer.write(dummyFilePath, data, true).unsafeRunSync()
    val result = io.Source.fromFile(dummyFilePath).getLines.toList.mkString("", "\n", "\n")

    val expected =
      """location,latitude,longitude,elevation,conditions,temperature,pressure,humidity,time
        |Melbourne|-37.814,144.96332,7.0|2019-06-15 20:47:53|Rain|+12.5|1010.3|97
        |Amsterdam|52.379189,4.899431,13.0|2019-06-15 20:47:57|Sunny|+30.5|998.4|96
        |Melbourne|-37.814,144.96332,7.0|2019-06-15 20:49:33|Sunny|+15.5|1010.3|97
        |Amsterdam|52.379189,4.899431,13.0|2019-06-15 20:49:33|Snow|+29.5|998.4|96
        |""".stripMargin

    result shouldBe expected
  }

  "write" should "fail to write empty data" in {
    val writer = new FileWriter {}

    intercept[RuntimeException](
      writer.write(dummyFilePath, Vector()).unsafeRunSync()
    ).getMessage shouldBe "Data is empty"
  }

  "write" should "fail with empty file path" in {
    val writer = new FileWriter {}

    val data = Vector(
      Vector(
        WeatherData("Melbourne", Coordinate(-37.814, 144.96332, 7.0),
          1560595773042L, Sunny, Temperature(15.5), 1010.3, 97
        )))

    intercept[RuntimeException](
      writer.write("", data).unsafeRunSync()
    ).getMessage shouldBe "File name can not be empty"
  }

}
