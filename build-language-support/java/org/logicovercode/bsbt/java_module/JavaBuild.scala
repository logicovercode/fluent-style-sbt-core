package org.logicovercode.bsbt.java_module

import org.logicovercode.bsbt.build.{Build, BuildApply}
import sbt.Keys._
import sbt._

case class JavaBuild(override val sbtSettings: Set[Def.Setting[_]]) extends Build[JavaBuild](sbtSettings) {

  override def moduleWithNewSettings(allSettings: Set[Def.Setting[_]]): JavaBuild = JavaBuild(allSettings)
}

object JavaBuild extends BuildApply[JavaBuild] {
  override def moduleWithNewSettings(allSettings: Set[Def.Setting[_]]): JavaBuild = JavaBuild(allSettings)

  override def apply(projectOrganization: String, projectArtifact: String, mavenVersion: String): JavaBuild = {

    val additionalJavaBuildSettings : Set[Def.Setting[_]] = Set(
      publishMavenStyle := true,
      crossPaths := false,
      autoScalaLibrary := false
    )

    val javaBuild = super.apply(projectOrganization, projectArtifact, mavenVersion)
    new JavaBuild(javaBuild.sbtSettings ++ additionalJavaBuildSettings)
  }
}
