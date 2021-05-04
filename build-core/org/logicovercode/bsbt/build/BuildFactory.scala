package org.logicovercode.bsbt.build

import sbt.Def

trait BuildFactory[T <: Build[T]]{
  def moduleWithNewSettings(sbtSettings: Set[Def.Setting[_]]) : T
}
