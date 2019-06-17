package au.com.ms.domain

case class Temperature(value: Double) {
  def sign: String = value match {
    case t if t > 0 => "+"
    case _ => ""
  }

  override def toString: String =
    sign + {
      if (value.toString.split('.').last == "0") value.toInt else value
    }
}
