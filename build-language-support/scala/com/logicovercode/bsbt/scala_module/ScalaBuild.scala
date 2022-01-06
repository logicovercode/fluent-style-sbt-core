package com.logicovercode.bsbt.scala_module

import com.logicovercode.bsbt.build.{Build, BuildInitialSettings, IBuild}
import com.logicovercode.bsbt.java_module.JavaBuild
import com.logicovercode.bsbt.build.BuildInitialSettings
import sbt.Def
import sbt.Keys.{autoScalaLibrary, crossPaths, crossScalaVersions, publishMavenStyle, scalaVersion}

trait IScalaBuild[T <: Build[T]] extends IBuild[T]{
  def scalaVersions(scalaVersionToUse : String, buildCrossScalaVersions : Seq[String]): T
}

case class ScalaBuild(override val sbtSettings: Set[Def.Setting[_]]) extends Build[ScalaBuild] with IScalaBuild[ScalaBuild] {

  override def moduleWithNewSettings(allSettings: Set[Def.Setting[_]]): ScalaBuild = ScalaBuild(allSettings)

  def scalaVersions(version : String, crossVersions : Seq[String] = Seq()): ScalaBuild = {
    val scalaVersionSettings = Set(scalaVersion := version)
    val crossScalaVersionSettings = Set(crossScalaVersions := crossVersions ++ Seq(version))
    //val crossPathsSettings = if(crossVersions.size > 0)   Set(crossPaths := true)   else  Set(crossPaths := false)
    ScalaBuild(this.sbtSettings ++ scalaVersionSettings ++ crossScalaVersionSettings)
  }
}

object ScalaBuild {
  def apply(projectOrganization: String, projectArtifact: String, mavenVersion: String): ScalaBuild = {

    val initialSettings = BuildInitialSettings.initialSettings(projectOrganization, projectArtifact, mavenVersion)
    new ScalaBuild(initialSettings)
  }
}