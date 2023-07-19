ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.0"

lazy val root = (project in file("."))
  .settings(
    name := "FtpClient",
    idePackagePrefix := Some("com.yuanta.sre.ftp"),
  )

libraryDependencies ++= Seq(
  "com.github.scopt" %% "scopt" % "4.0.1",
  "commons-net" % "commons-net" % "3.6"
)
