name := "ScalaCamp"

version := "0.1"

scalaVersion := "2.12.8"

scalacOptions ++= Seq(
  "-encoding", "utf8"
)

libraryDependencies += "org.typelevel" %% "cats-core" % "1.6.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.7" % Test