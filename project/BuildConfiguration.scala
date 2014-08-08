import sbt.Keys._
import sbt._

object BuildConfiguration extends Build {

  override def settings: Seq[Def.Setting[_]] = super.settings ++ Seq(
    organization := "si.urbas",
    scalaVersion := "2.11.1"
  )

  lazy val root = Project(
    id = "chrony-root",
    base = file("."),
    aggregate = Seq(chrony)
  )

  lazy val chrony = Project(
    id = "chrony",
    base = file("chrony"),
    settings = Seq(
      libraryDependencies ++= Seq(
        "com.novocode" % "junit-interface" % "0.10" % "test",
        "junit" % "junit" % "4.11" % "test"
      )
    )
  )

}