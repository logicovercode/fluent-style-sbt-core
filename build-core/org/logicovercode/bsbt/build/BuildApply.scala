package org.logicovercode.bsbt.build

import sbt.Def
import sbt.Keys._

trait BuildApply[T <: Build[T]] extends BuildFactory[T] {
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
      offline := sbtOfflineMode,
      publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)
    )

    moduleWithNewSettings(defaultSettings)
  }
}