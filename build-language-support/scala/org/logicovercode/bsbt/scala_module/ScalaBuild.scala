package org.logicovercode.bsbt.scala_module

import org.logicovercode.bsbt.build.{BuildApply, Build}
import sbt.{Def, _}
import sbt.Keys._

case class ScalaBuild(override val sbtSettings: Set[Def.Setting[_]]) extends Build[ScalaBuild](sbtSettings) {

  override def moduleWithNewSettings(allSettings: Set[Def.Setting[_]]): ScalaBuild = ScalaBuild(allSettings)

  def moduleScalaVersion(_scalaVersion: String): ScalaBuild = {
    val scalaVersionSettings = Set(scalaVersion := _scalaVersion)
    ScalaBuild(this.sbtSettings ++ scalaVersionSettings)
  }
}

object ScalaBuild extends BuildApply[ScalaBuild]{
  override def moduleWithNewSettings(allSettings: Set[Def.Setting[_]]): ScalaBuild = ScalaBuild(allSettings)
}