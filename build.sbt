name := """event-queue"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "com.rabbitmq" % "amqp-client" % "3.5.2",
  "com.google.inject" % "guice" % "4.0-beta",
  "com.nimbusds" % "nimbus-jose-jwt" % "2.22.1"
)