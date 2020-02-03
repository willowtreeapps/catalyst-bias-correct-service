enablePlugins(JavaAppPackaging)

name := """bias-correct-service"""
organization := "org.catalyst"

version := sys.env.getOrElse("VERSION_NUMBER", "1.0-SNAPSHOT")

lazy val root = (project in file(".")).enablePlugins(PlayJava, JavaAgent)

scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
  guice,
  "org.apache.commons" % "commons-csv" % "1.7",
  "org.apache.opennlp" % "opennlp-tools" % "1.9.1",
  "org.javatuples" % "javatuples" % "1.2",
  "io.kamon" %% "kamon-bundle" % "2.0.4",
  "io.kamon" %% "kamon-prometheus" % "2.0.0"
)

// Docker settings
import com.typesafe.sbt.packager.docker._

dockerPermissionStrategy := DockerPermissionStrategy.Run
dockerVersion := Some(DockerVersion(18, 9, 0, Some("ce")))
dockerRepository := sys.env.get("DOCKER_REPOSITORY")
dockerBaseImage := "openjdk:12-alpine"
dockerCommands ++= Seq(
  Cmd("USER", "root"),
  ExecCmd("RUN", "apk", "add", "udev"),
  ExecCmd("RUN", "apk", "add", "--no-cache", "bash"),
)
