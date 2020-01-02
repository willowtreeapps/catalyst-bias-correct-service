enablePlugins(JavaAppPackaging)

name := """bias-correct-service"""
organization := "org.catalyst"

version := sys.env.getOrElse("VERSION_NUMBER", "1.0-SNAPSHOT")

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
  guice,
  "org.apache.commons" % "commons-csv" % "1.7",
  "org.apache.opennlp" % "opennlp-tools" % "1.9.1",
  "org.apache.tika" % "tika-langdetect" % "1.23",
  "org.javatuples" % "javatuples" % "1.2"
)

// Docker settings
import com.typesafe.sbt.packager.docker._

dockerBaseImage := "adoptopenjdk/openjdk12:x86_64-alpine-jre-12.33"
dockerCommands ++= Seq(
  Cmd("USER", "root"),
  ExecCmd("RUN", "apk", "add", "--no-cache", "bash"),
)
