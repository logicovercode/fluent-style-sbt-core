package com.logicovercode.bsbt.java_module

import com.logicovercode.bsbt.build.{Build, BuildInitialSettings, IBuild, OutputVersion}
import sbt.Keys._
import sbt._

trait IJavaBuild[T <: Build[T]] extends IBuild[T]{
  def scalaVersionInTestScope(scalaVersion : String): T
}

case class JavaBuild(override val sbtSettings: Set[Def.Setting[_]]) extends Build[JavaBuild]  with IJavaBuild[JavaBuild]{

  override def moduleWithNewSettings(allSettings: Set[Def.Setting[_]]): JavaBuild = JavaBuild(allSettings)

  override def javaCompatibility(outputVersion: OutputVersion): JavaBuild = javaCompatibility(outputVersion.jSource(), outputVersion.jTarget())

  override def javaCompatibility(source: String, target: String): JavaBuild = {
    val javacOptionSettings : Set[Def.Setting[_]] = Set(
      Compile / compile / javacOptions ++= Seq("-source", source, "-target", target)
    )

    new JavaBuild(this.sbtSettings ++ javacOptionSettings)
  }

  override def scalaVersionInTestScope(version: String): JavaBuild = {

    val testScopeScalaVersionSettings : Set[Def.Setting[_]] = Set(
      scalaVersion := version,
      libraryDependencies += "org.scala-lang" % "scala-library" % version % Test
    )

    new JavaBuild(this.sbtSettings ++ testScopeScalaVersionSettings)
  }
}

object JavaBuild {

  def apply(projectOrganization: String, projectArtifact: String, mavenVersion: String): JavaBuild = {

    val additionalJavaBuildSettings : Set[Def.Setting[_]] = Set(
      crossPaths := false,
      autoScalaLibrary := false
    )

    val initialSettings = BuildInitialSettings.initialSettings(projectOrganization, projectArtifact, mavenVersion)
    new JavaBuild(initialSettings ++ additionalJavaBuildSettings)
  }
}
