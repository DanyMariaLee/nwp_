package au.com.ms.parser

import au.com.ms.app.NWPConfig
import cats.effect.IO
import pureconfig.generic.auto._

trait ConfigParser {

  def parseConfig = pureconfig.loadConfig[NWPConfig] match {
    case Left(e) => IO.raiseError(new RuntimeException(s"Failed to parse config: $e"))
    case Right(c) => IO.pure(c)
  }


}
