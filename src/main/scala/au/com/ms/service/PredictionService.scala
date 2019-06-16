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
                 result: Vector[Vector[WeatherData]] = Vector()
                ): IO[Vector[Vector[WeatherData]]] = {
      weather.headOption match {
        case Some(w) if w.time + step <= endDate =>
          val predicted = weather.map(predict(_, step))
          forecast(predicted, step, endDate, result :+ predicted)
        case _ => IO.pure(result)
      }
    }

    forecast(weather, step, endDate, Vector())
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
  private[service] def predict(weather: WeatherData, step: Long): WeatherData = {
    WeatherData(
      weather.location,
      weather.position,
      weather.time + step,
      genCondition(weather.condition),
      Temperature(gen(weather.temperature.value, -30, 30, 0.5)),
      gen(weather.pressure, 800, 1400, 20),
      gen(weather.humidity, 1, 100, 1).toInt
    )
  }

  private[service] def genCondition(c: Condition) =
    c match {
      case Rain => Snow
      case Snow => Sunny
      case _ => Rain
    }

  private[service] def gen(value: Double, min: Double, max: Double, dx: Double) =
    value match {
      case t if t - dx > min => t - dx
      case _ => max
    }

}
