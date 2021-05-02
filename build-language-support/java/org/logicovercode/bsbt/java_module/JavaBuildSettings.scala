package org.logicovercode.bsbt.java_module

import org.logicovercode.bsbt.core_module.{BuildApply, BuildSettings}
import sbt.Def

case class JavaBuildSettings(override val sbtSettings: Set[Def.Setting[_]]) extends BuildSettings[JavaBuildSettings](sbtSettings) {

  override def moduleWithNewSettings(allSettings: Set[Def.Setting[_]]): JavaBuildSettings = JavaBuildSettings(allSettings)
}

object JavaBuildSettings extends BuildApply[JavaBuildSettings]{
  override def moduleWithNewSettings(allSettings: Set[Def.Setting[_]]): JavaBuildSettings = JavaBuildSettings(allSettings)
}

