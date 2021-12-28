package com.logicovercode.bsbt.scala_module

import com.logicovercode.bsbt.build.{Build, BuildInitialSettings, IBuild}
import com.logicovercode.bsbt.java_module.JavaBuild
import com.logicovercode.bsbt.build.BuildInitialSettings
import sbt.Def
import sbt.Keys.{autoScalaLibrary, crossPaths, publishMavenStyle, scalaVersion}

trait IScalaBuild[T <: Build[T]] extends IBuild[T]{
  def moduleScalaVersion(_scalaVersion: String): T
}

case class ScalaBuild(override val sbtSettings: Set[Def.Setting[_]]) extends Build[ScalaBuild] with IScalaBuild[ScalaBuild] {

  override def moduleWithNewSettings(allSettings: Set[Def.Setting[_]]): ScalaBuild = ScalaBuild(allSettings)

  def moduleScalaVersion(_scalaVersion: String): ScalaBuild = {
    val scalaVersionSettings = Set(scalaVersion := _scalaVersion)
    ScalaBuild(this.sbtSettings ++ scalaVersionSettings)
  }
}

object ScalaBuild {
  def apply(projectOrganization: String, projectArtifact: String, mavenVersion: String): ScalaBuild = {

    val initialSettings = BuildInitialSettings.initialSettings(projectOrganization, projectArtifact, mavenVersion)
    new ScalaBuild(initialSettings)
  }
}