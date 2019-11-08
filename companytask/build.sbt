name := """CompanyTask"""
organization := "com.company"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.0"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test

//Slick dependency
libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "4.0.2",
  "com.typesafe.play" %% "play-slick-evolutions" % "4.0.2"
)

//MySQL connector
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.18"

//Bootstrap dependency
libraryDependencies += "org.webjars" % "bootstrap" % "3.3.6"



// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.company.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.company.binders._"
