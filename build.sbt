name := "debug"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "com.lihaoyi" %% "pprint" % "0.5.3",
  "com.lihaoyi" %% "sourcecode" % "0.1.5",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "org.scalatest" %% "scalatest" % "3.0.5" % Test
)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)