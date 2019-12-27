name := """bias-correct-service"""
organization := "org.catalyst"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
  guice,
  "org.apache.commons" % "commons-csv" % "1.7",
  "org.apache.opennlp" % "opennlp-tools" % "1.9.1",
  "org.apache.tika" % "tika-langdetect" % "1.23",
  "org.javatuples" % "javatuples" % "1.2"
)
