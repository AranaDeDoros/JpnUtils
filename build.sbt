import Dependencies._
import sbt.Keys.libraryDependencies

import scala.collection.immutable.Seq

ThisBuild / scalaVersion     := "2.13.7"
ThisBuild / version          := "1.0.0"
ThisBuild / organization     := "com.aranadedoros"
ThisBuild / organizationName := "AranaDeDoros"


Compile / doc / scalacOptions ++= Seq(
  "-skip-packages", "main"
)
Compile / packageBin / mappings := {
  val original = (Compile / packageBin / mappings).value
  original.filterNot { case (_, pathInJar) =>
    pathInJar.startsWith("main/")
  }
}

lazy val root = (project in file("."))
  .settings(
    name := "JpnUtils",
    //libraryDependencies += scalaTest % Test
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "1.0.0" % Test,
      "org.scalameta" %% "munit-scalacheck" % "1.0.0" % Test
    )
  )

