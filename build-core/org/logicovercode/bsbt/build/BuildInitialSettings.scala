package org.logicovercode.bsbt.build

import sbt._
import sbt.Keys._


object BuildInitialSettings {


  def initialSettings(projectOrganization: String,
             projectArtifact: String,
             mavenVersion: String
           ): Set[Def.Setting[_]] = {

    val sbtOfflineMode = sys.env.getOrElse("SBT_OFFLINE_MODE", "false").toBoolean

    val defaultSettings: Set[Def.Setting[_]] = Set(
      name := projectArtifact,
      version := mavenVersion,
      organization := projectOrganization,
      offline := sbtOfflineMode,
      publishConfiguration := publishConfiguration.value.withOverwrite(true),
      publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)
    )

    defaultSettings
  }
}