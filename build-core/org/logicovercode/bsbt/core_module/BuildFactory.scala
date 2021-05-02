package org.logicovercode.bsbt.core_module

import sbt.Def

trait BuildFactory[T <: BuildSettings[T]]{
  def moduleWithNewSettings(sbtSettings: Set[Def.Setting[_]]) : T
}
