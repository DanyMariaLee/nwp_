package au.com.ms.domain

import java.text.SimpleDateFormat

case class WeatherData(location: String,
                       position: Coordinate,
                       time: Long,
                       condition: Condition,
                       temperature: Temperature,
                       pressure: Double,
                       humidity: Int
                      ) {

  import WeatherData.sdf

  /** This function will produce the string formatted as required:
    *
    * Sydney|-33.86,151.21,39|2015-12-23 16:02:12|Rain|+12.5|1004.3|97
    */
  def show: String =
    List(
      location.capitalize,
      position.toString,
      sdf.format(time),
      condition,
      temperature.toString,
      pressure,
      humidity
    ).mkString("|")
}

object WeatherData {
  private val format = "yyyy-MM-dd HH:mm:ss"
  private val sdf = new SimpleDateFormat(format)
}