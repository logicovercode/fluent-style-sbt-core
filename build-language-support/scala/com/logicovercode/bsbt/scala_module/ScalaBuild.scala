package com.logicovercode.bsbt.scala_module

import com.logicovercode.bsbt.build.{Build, BuildInitialSettings, IBuild, OutputVersion}
import sbt.Keys._
import sbt._

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

  override def javaCompatibility(outputVersion: OutputVersion): ScalaBuild = javaCompatibility(outputVersion.jSource(), outputVersion.sTarget())

  override def javaCompatibility(source: String, target: String): ScalaBuild = {

    val javacOptionsSeq = Seq("-source", source, "-target", target)
    println(s"javac options >$javacOptionsSeq<")

    val javacOptionSettings : Set[Def.Setting[_]] = Set(
      Compile / compile / javacOptions ++= javacOptionsSeq
    )

    val scalaSource = source match {
      case "1.8" => "8"
      case _ => source
    }

    val scalaTarget = source match {
      case "1.8" => "jvm-1.8"
      case _ => target
    }

    val scalacOptionsSeq = Seq("-release", scalaSource, s"-target:$scalaTarget")
    println(s"scalac options >$scalacOptionsSeq<")


    val scalacOptionSettings : Set[Def.Setting[_]] = Set(
      Compile / compile / scalacOptions ++= scalacOptionsSeq
    )

    new ScalaBuild(this.sbtSettings ++ javacOptionSettings ++ scalacOptionSettings)
  }
}

object ScalaBuild {
  def apply(projectOrganization: String, projectArtifact: String, mavenVersion: String): ScalaBuild = {

    val initialSettings = BuildInitialSettings.initialSettings(projectOrganization, projectArtifact, mavenVersion)
    new ScalaBuild(initialSettings)
  }

  def apply(projectOrganization: String, mavenVersion: String): ScalaBuild = {

    val initialSettings = BuildInitialSettings.initialSettings(projectOrganization, mavenVersion)
    new ScalaBuild(initialSettings)
  }
}