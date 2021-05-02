package org.logicovercode.bsbt.scala_module

import org.logicovercode.bsbt.core_module.{BuildApply, BuildSettings}
import sbt.{Def, _}
import sbt.Keys._

case class ScalaBuildSettings(override val sbtSettings: Set[Def.Setting[_]]) extends BuildSettings[ScalaBuildSettings](sbtSettings) {

  override def moduleWithNewSettings(allSettings: Set[Def.Setting[_]]): ScalaBuildSettings = ScalaBuildSettings(allSettings)

  def moduleScalaVersion(_scalaVersion: String): ScalaBuildSettings = {
    val scalaVersionSettings = Set(scalaVersion := _scalaVersion)
    ScalaBuildSettings(this.sbtSettings ++ scalaVersionSettings)
  }
}

object ScalaBuildSettings extends BuildApply[ScalaBuildSettings]{
  override def moduleWithNewSettings(allSettings: Set[Def.Setting[_]]): ScalaBuildSettings = ScalaBuildSettings(allSettings)
}