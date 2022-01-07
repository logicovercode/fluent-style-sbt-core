package com.logicovercode.bsbt.build

import sbt._
import sbt.Keys._

import scala.util.Try


object BuildInitialSettings {


  def initialSettings(projectOrganization: String,
             projectArtifact: String,
             mavenVersion: String
           ): Set[Def.Setting[_]] = {

    val sbtOfflineMode = Try(sys.env("SBT_OFFLINE_MODE").toBoolean).getOrElse(false)

    val defaultSettings: Set[Def.Setting[_]] = Set(
      name := projectArtifact,
      version := mavenVersion,
      organization := projectOrganization,
      offline := sbtOfflineMode,
      publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true),
      //IMPORTANT : publishConfiguration is not set to true, because
      //we don't want to overwrite artifact on sonatype, instead we want to get an error
      //if republish attempted, when same version is already there on sonatype
      //publishConfiguration := publishConfiguration.value.withOverwrite(true),
      scalacOptions ++= Seq("-deprecation", "-feature")
    )

    defaultSettings
  }
}