package au.com.ms.fileutils

import java.io.{File, PrintWriter}

import au.com.ms.domain.WeatherData
import cats.effect.IO

trait FileWriter {

  /** Writes data next to the app.jar */
  def write(fileName: String, data: Vector[Vector[WeatherData]]): IO[Unit] = IO {
    val writer = new PrintWriter(new File(fileName))
    data.foreach(w => writer.write(w.map(_.show).mkString("","\n","\n")))
    writer.close()
  }
}