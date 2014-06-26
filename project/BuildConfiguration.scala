import sbt._
import sbt.Keys._

object BuildConfiguration extends Build {
  override def settings: Seq[Def.Setting[_]] = super.settings ++ Seq(
    organization := "si.urbas"
  )

  lazy val root = Project(
    id = "chrony-root",
    base = file("."),
    aggregate = Seq(chrony)
  )

  lazy val chrony = Project(
    id = "chrony",
    base = file("chrony")
  )
}