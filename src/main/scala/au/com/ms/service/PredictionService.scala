package au.com.ms.service

import au.com.ms.domain.Condition.{Rain, Snow, Sunny}
import au.com.ms.domain._
import cats.effect.IO

trait PredictionService {

  def forecast(weather: Vector[WeatherData], step: Long, endDate: Long): IO[Vector[Vector[WeatherData]]] = {

    // this inner cycle is here to avoid StackOverFlow error
    @scala.annotation.tailrec
    def forecast(weather: Vector[WeatherData],
                 step: Long,
                 endDate: Long,
                 prevData: Vector[WeatherData],
                 result: Vector[Vector[WeatherData]] = Vector()
                ): IO[Vector[Vector[WeatherData]]] = {
      weather.headOption match {
        case Some(w) if w.time + step <= endDate =>

          val predicted = weather.map(wd =>
            predict(wd, step, prevData.find(_.location == wd.location)))

          forecast(
            predicted,
            step,
            endDate,
            weather,
            result :+ predicted
          )
        case _ => IO.pure(result)
      }
    }

    weather.headOption match {
      case Some(w) if w.time + step <= endDate => forecast(weather, step, endDate, Vector())
      case Some(w) if w.time > endDate => IO.raiseError(
        new RuntimeException("Weather prediction is available for `end-date` > start date in input data file." +
          s"Current parameters: `end-date` = $endDate, start date: ${w.time}")
      )
      case Some(w) if w.time + step > endDate => IO.raiseError(
        new RuntimeException("Calculation step is too big for provided date range"))
      case None => IO.raiseError(new RuntimeException("Input weather data is empty"))
    }
  }

  /**
    * This function does simple data manipulation to make sure
    * the environment evolves over time:
    *
    * temperature T[-30, 30], dT = 0.5
    * pressure P[800, 1400], dP = 20
    * humidity H[1, 100], dH = 1
    *
    * formula for time: t2 = t1 + dt, dt = step
    */
  private[service] def predict(weather: WeatherData,
                               step: Long,
                               prevWeatherValue: Option[WeatherData] = None
                              ): WeatherData = {
    WeatherData(
      weather.location,
      weather.position,
      weather.time + step,
      genCondition(weather.condition),
      Temperature(
        gen(weather.temperature.value, -30, 30, 0.5, prevWeatherValue.map(_.temperature.value))
      ),
      gen(weather.pressure, 800, 1400, 20, prevWeatherValue.map(_.pressure)),
      gen(weather.humidity, 1, 100, 1, prevWeatherValue.map(_.humidity)).toInt
    )
  }

  private[service] def genCondition(c: Condition) =
    c match {
      case Rain => Snow
      case Snow => Sunny
      case _ => Rain
    }

  private[service] def gen(value: Double, min: Double, max: Double, dv: Double, prevValue: Option[Double] = None) = {
    val v1 = value - dv
    val v2 = value + dv

    if (prevValue.exists(_ < value)) {
      if (v2 < max) v2 else v1
    } else if (v1 >= min) v1 else v2
  }

}
