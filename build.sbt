name := "nwp"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-effect" % "1.3.1",
  "org.typelevel" %% "cats-core" % "1.3.1",
  "com.github.pureconfig" %% "pureconfig" % "0.11.1",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)