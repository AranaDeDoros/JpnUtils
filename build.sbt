import Dependencies._

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
    libraryDependencies += scalaTest % Test
  )

