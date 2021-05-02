package org.logicovercode.bsbt.core_module

import sbt.Def
import sbt.Keys._

trait BuildApply[T <: BuildSettings[T]] extends BuildFactory[T] {
  def apply(
             projectOrganization: String,
             projectArtifact: String,
             mavenVersion: String
           ): T = {

    val sbtOfflineMode = sys.env.getOrElse("SBT_OFFLINE_MODE", "false").toBoolean

    val defaultSettings: Set[Def.Setting[_]] = Set(
      name := projectArtifact,
      version := mavenVersion,
      organization := projectOrganization,
      offline := sbtOfflineMode
    )

    moduleWithNewSettings(defaultSettings)
  }
}