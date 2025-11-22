import Dependencies._

ThisBuild / scalaVersion     := "2.13.7"
ThisBuild / version          := "1.0.0"
ThisBuild / organization     := "com.aranadedoros"
ThisBuild / organizationName := "AranaDeDoros"
Compile / doc / unmanagedSourceDirectories := {
  (Compile / scalaSource).value / "lib" :: Nil
}

lazy val root = (project in file("."))
  .settings(
    name := "JpnUtils",
    libraryDependencies += scalaTest % Test
  )

