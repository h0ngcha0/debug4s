name := "debug4s"
organization := "it.softfork"

scalaVersion := "2.12.8"

scalacOptions ++= Seq("-deprecation", "-feature")

libraryDependencies ++= Seq(
  "com.lihaoyi" %% "pprint" % "0.6.6",
  "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  "org.scala-lang" % "scala-compiler" % scalaVersion.value,
  "org.scalatest" %% "scalatest" % "3.2.9" % Test
)

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))