package au.com.ms.fileutils

import java.io.{File, PrintWriter}

import au.com.ms.domain.WeatherData
import cats.effect.IO

trait FileWriter {

  def write(fileName: String,
            data: Vector[Vector[WeatherData]],
            addHeader: Boolean = true
           ): IO[Unit] = {
    if (fileName.isEmpty) IO.raiseError(
      new RuntimeException("File name can not be empty"))
    else {
      val writer = IO(new PrintWriter(new File(fileName)))

      data.length match {
        case 0 => IO.raiseError(new RuntimeException("Data is empty"))
        case _ =>
          writer.map { w =>
            if (addHeader) w.write(WeatherData.columnNames)
            data.foreach(d => w.write(d.map(_.show).mkString("", "\n", "\n")))
            w.close()
          }
      }
    }
  }
}