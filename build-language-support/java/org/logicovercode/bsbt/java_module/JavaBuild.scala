package org.logicovercode.bsbt.java_module

import org.logicovercode.bsbt.build.{BuildApply, Build}
import sbt.Def

case class JavaBuild(override val sbtSettings: Set[Def.Setting[_]]) extends Build[JavaBuild](sbtSettings) {

  override def moduleWithNewSettings(allSettings: Set[Def.Setting[_]]): JavaBuild = JavaBuild(allSettings)
}

object JavaBuild extends BuildApply[JavaBuild]{
  override def moduleWithNewSettings(allSettings: Set[Def.Setting[_]]): JavaBuild = JavaBuild(allSettings)
}

