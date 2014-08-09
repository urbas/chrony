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
    settings = Seq(libraryDependencies ++= basicJavaTestDependencies)
  )

  private val basicJavaTestDependencies = Seq(
    "com.novocode" % "junit-interface" % "0.10" % "test",
    "org.hamcrest" % "hamcrest-all" % "1.3",
    "org.hamcrest" % "hamcrest-core" % "1.3",
    ("junit" % "junit-dep" % "4.11")
      .exclude("org.hamcrest", "hamcrest-core"),
    ("org.mockito" % "mockito-all" % "1.9.5")
      .exclude("org.hamcrest", "hamcrest-core"),
    ("org.mockito" % "mockito-core" % "1.9.5")
      .exclude("org.hamcrest", "hamcrest-core")
  )

}