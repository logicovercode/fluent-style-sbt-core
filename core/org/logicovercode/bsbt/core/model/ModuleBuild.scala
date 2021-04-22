package org.logicovercode.bsbt.core.model

import org.logicovercode.bsbt.docker.DockerSettings
import org.logicovercode.bsbt.docker.model.IDockerService
import sbt.Keys._
import sbt._

case class ModuleBuild private (private val allSettings: Set[Def.Setting[_]]) extends BuildModuleSettings[ModuleBuild] with DockerSettings {

  def sbtOffLineMode(sbtOffLineMode: Boolean): ModuleBuild = {
    val _settings = Set(
      offline := sbtOffLineMode
    )
    ModuleBuild(this.allSettings ++ _settings)
  }

  @Deprecated
  override def moduleSourceDirectories(
      projectSourceDirectories: String*
  ): ModuleBuild = sourceDirectories(projectSourceDirectories: _*)

  def sourceDirectories(projectSourceDirectories: String*): ModuleBuild = {
    val _settings = Set(
      unmanagedSourceDirectories in Compile ++= projectSourceDirectories.map { src =>
        (baseDirectory in Compile).value / src
      }
    )
    ModuleBuild(this.allSettings ++ _settings)
  }

  @Deprecated
  override def moduleResourceDirectories(
      projectResourceDirectories: String*
  ): ModuleBuild = resourceDirectories(projectResourceDirectories: _*)
  def resourceDirectories(projectResourceDirectories: String*): ModuleBuild = {
    val _settings = Set(
      unmanagedResourceDirectories in Compile ++= projectResourceDirectories
        .map { res => (baseDirectory in Compile).value / res }
    )
    ModuleBuild(this.allSettings ++ _settings)
  }

  def testSourceDirectories(testSourceDirectories: String*): ModuleBuild = {
    val _settings = Set(
      unmanagedSourceDirectories in Test ++= testSourceDirectories.map { src =>
        (baseDirectory in Test).value / src
      }
    )
    ModuleBuild(this.allSettings ++ _settings)
  }

  def testResourceDirectories(testResourceDirectories: String*): ModuleBuild = {
    val _settings = Set(
      unmanagedResourceDirectories in Test ++= testResourceDirectories.map { res =>
        (baseDirectory in Test).value / res
      }
    )
    ModuleBuild(this.allSettings ++ _settings)
  }

  def sbtPlugins(sbtPlugins: ModuleID*): ModuleBuild = {
    val sbtPluginsSet: Set[sbt.ModuleID] = sbtPlugins.toSet
    val moduleIdSettings: Set[Def.Setting[_]] =
      sbtPluginsSet.map(mID => addSbtPlugin(mID))
    ModuleBuild(this.allSettings ++ moduleIdSettings)
  }

  @Deprecated
  override def moduleDependencies(
      projectDependencies: Seq[JvmModuleID]*
  ): ModuleBuild = dependencies(projectDependencies: _*)

  def dependencies(projectDependencies: Seq[JvmModuleID]*): ModuleBuild = {
    val jvmModuleIdSet = projectDependencies.flatten.toSet
    ModuleBuild(
      this.allSettings ++ moduleIdSettings(
        jvmModuleIdSet.map(_.moduleID)
      ) ++ resolverSettingsSet(jvmModuleIdSet) ++ allSettings
    )
  }

  def testDependencies(projectDependencies: Seq[JvmModuleID]*): ModuleBuild = {
    val jvmModuleIdSet = projectDependencies.flatten.toSet
    val (scopedJvmModuleIds, unscopedJvmModuleIds) =
      jvmModuleIdSet.partition(jvmID => jvmID.moduleID.configurations.isDefined)
    val effectiveModuleIds = scopedJvmModuleIds.map(
      _.moduleID
    ) ++ unscopedJvmModuleIds.map(jvmModuleID => jvmModuleID.moduleID % Test)
    ModuleBuild(
      this.allSettings ++ moduleIdSettings(
        effectiveModuleIds
      ) ++ resolverSettingsSet(jvmModuleIdSet)
    )
  }

  def dockerServices(dockerServices: IDockerService*): ModuleBuild = {
    //this will remove duplicates
    val dockerInstancesSet = (dockerServices map (_.instance())).toSet
    val settings: Seq[Def.Setting[_]] = Seq(
      dependentDockerServices := dockerInstancesSet
    )

    val containerSettings = dockerServices.map(_.sbtSettings()).flatten

    val allSettings = this.settings ++ settings ++ containerSettings

    ModuleBuild(allSettings.toSet)
  }

  private def resolverSettingsSet(
      dependencies: Set[JvmModuleID]
  ): Set[Def.Setting[_]] = {
    val dependencyResolvers =
      dependencies.map(dep => dep.resolverForThisModuleId).flatten
    Set(resolvers ++= dependencyResolvers.toSeq)
  }

  private def moduleIdSettings(
      dependencies: Set[ModuleID]
  ): Set[Def.Setting[_]] = {
    Set(libraryDependencies ++= dependencies.toSeq)
  }

  def settings: Seq[Def.Setting[_]] = {
    this.allSettings.toSeq ++ Set(
      publishLocalConfiguration := publishLocalConfiguration.value
        .withOverwrite(true)
    )
  }

  override def moduleScalaVersion(moduleScalaVersion: String): ModuleBuild = {
    val scalaVersionSettings = Set(scalaVersion := moduleScalaVersion)
    ModuleBuild(this.allSettings ++ scalaVersionSettings)
  }
}

object ModuleBuild {

  def apply(
      projectOrganization: String,
      projectArtifact: String,
      mavenVersion: String
  ): ModuleBuild = {
    //user shouldn't be forced to set this variable
    val sbtOfflineMode =
      sys.env.get("SBT_OFFLINE_MODE").getOrElse("false").toBoolean

    val defaultSettings: Set[Def.Setting[_]] = Set(
      name := projectArtifact,
      version := mavenVersion,
      organization := projectOrganization,
      offline := sbtOfflineMode
    )
    new ModuleBuild(defaultSettings)
  }
}
