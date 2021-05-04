package org.logicovercode.bsbt.scala_module

import org.logicovercode.bsbt.build.{Build, BuildApply, IBuild}
import sbt.Def
import sbt.Keys.scalaVersion

trait IScalaBuild[T <: Build[T]] extends IBuild[T]{
  def moduleScalaVersion(_scalaVersion: String): T
}

case class ScalaBuild(override val sbtSettings: Set[Def.Setting[_]]) extends Build[ScalaBuild] with IScalaBuild[ScalaBuild] {

  override def moduleWithNewSettings(allSettings: Set[Def.Setting[_]]): ScalaBuild = ScalaBuild(allSettings)

  def moduleScalaVersion(_scalaVersion: String): ScalaBuild = {
    val scalaVersionSettings = Set(scalaVersion := _scalaVersion)
    ScalaBuild(this.sbtSettings ++ scalaVersionSettings)
  }
}

object ScalaBuild extends BuildApply[ScalaBuild]{
  override def moduleWithNewSettings(allSettings: Set[Def.Setting[_]]): ScalaBuild = ScalaBuild(allSettings)
}