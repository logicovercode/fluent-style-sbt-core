package org.logicovercode.bsbt.java_module

import org.logicovercode.bsbt.build.{Build, BuildInitialSettings}
import sbt.Keys._
import sbt._

case class JavaBuild(override val sbtSettings: Set[Def.Setting[_]]) extends Build[JavaBuild] {

  override def moduleWithNewSettings(allSettings: Set[Def.Setting[_]]): JavaBuild = JavaBuild(allSettings)
}

object JavaBuild {

  def apply(projectOrganization: String, projectArtifact: String, mavenVersion: String): JavaBuild = {

    val additionalJavaBuildSettings : Set[Def.Setting[_]] = Set(
      publishMavenStyle := true,
      crossPaths := false,
      autoScalaLibrary := false
    )

    val initialSettings = BuildInitialSettings.initialSettings(projectOrganization, projectArtifact, mavenVersion)
    new JavaBuild(initialSettings ++ additionalJavaBuildSettings)
  }
}
