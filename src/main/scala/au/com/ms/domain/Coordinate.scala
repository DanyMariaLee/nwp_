package au.com.ms.domain

case class Coordinate(latitude: Double, longitude: Double, elevation: Double) {

  override def toString: String = List(latitude, longitude, elevation).mkString(",")
}
