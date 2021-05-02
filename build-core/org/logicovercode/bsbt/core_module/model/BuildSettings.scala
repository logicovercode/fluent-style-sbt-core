package org.logicovercode.bsbt.core_module.model

import org.logicovercode.bsbt.docker.DockerSettings
import org.logicovercode.bsbt.docker.model.IDockerService
import sbt.Keys._
import sbt._

abstract class BuildSettings[T <: BuildSettings[T]] ( val sbtSettings: Set[Def.Setting[_]]) extends IBuildSettings[T] with DockerSettings {

  def sbtOffLineMode(sbtOffLineMode: Boolean): T = {
    val _settings = Set(
      offline := sbtOffLineMode
    )
    moduleWithNewSettings(this.sbtSettings ++ _settings)
  }

  def moduleWithNewSettings(allSettings: Set[Def.Setting[_]]) : T

  def sourceDirectories(projectSourceDirectories: String*): T = {
    val _settings = Set(
       Compile / unmanagedSourceDirectories ++= projectSourceDirectories.map { src =>
        (Compile / baseDirectory).value / src
      }
    )
    moduleWithNewSettings(this.sbtSettings ++ _settings)
  }

  def resourceDirectories(projectResourceDirectories: String*): T = {
    val _settings = Set(
        Compile / unmanagedResourceDirectories ++= projectResourceDirectories
        .map { res => (Compile / baseDirectory).value / res }
    )
    moduleWithNewSettings(this.sbtSettings ++ _settings)
  }

  def testSourceDirectories(testSourceDirectories: String*): T = {
    val _settings = Set(
      Test / unmanagedSourceDirectories ++= testSourceDirectories.map { src =>
        (Test / baseDirectory).value / src
      }
    )
    moduleWithNewSettings(this.sbtSettings ++ _settings)
  }

  def testResourceDirectories(testResourceDirectories: String*): T = {
    val _settings = Set(
      Test / unmanagedResourceDirectories ++= testResourceDirectories.map { res =>
        (Test / baseDirectory).value / res
      }
    )
    moduleWithNewSettings(this.sbtSettings ++ _settings)
  }

  def sbtPlugins(sbtPlugins: ModuleID*): T = {
    val sbtPluginsSet: Set[sbt.ModuleID] = sbtPlugins.toSet
    val moduleIdSettings: Set[Def.Setting[_]] =
      sbtPluginsSet.map(mID => addSbtPlugin(mID))
    moduleWithNewSettings(this.sbtSettings ++ moduleIdSettings)
  }

  def dependencies(projectDependencies: Seq[JvmModuleID]*): T = {
    val jvmModuleIdSet = projectDependencies.flatten.toSet
    moduleWithNewSettings(
      this.sbtSettings ++ moduleIdSettings(
        jvmModuleIdSet.map(_.moduleID)
      ) ++ resolverSettingsSet(jvmModuleIdSet) ++ sbtSettings
    )
  }

  def testDependencies(projectDependencies: Seq[JvmModuleID]*): T = {
    val jvmModuleIdSet = projectDependencies.flatten.toSet
    val (scopedJvmModuleIds, unscopedJvmModuleIds) =
      jvmModuleIdSet.partition(jvmID => jvmID.moduleID.configurations.isDefined)
    val effectiveModuleIds = scopedJvmModuleIds.map(
      _.moduleID
    ) ++ unscopedJvmModuleIds.map(jvmModuleID => jvmModuleID.moduleID % Test)
    moduleWithNewSettings(
      this.sbtSettings ++ moduleIdSettings(
        effectiveModuleIds
      ) ++ resolverSettingsSet(jvmModuleIdSet)
    )
  }

  def dockerServices(dockerServices: IDockerService*): T = {
    //this will remove duplicates
    val dockerInstancesSet = (dockerServices map (_.instance())).toSet
    val settings: Seq[Def.Setting[_]] = Seq(
      dependentDockerServices := dockerInstancesSet
    )

    val containerSettings = dockerServices.flatMap(_.sbtSettings())

    val allSettings = this.settings ++ settings ++ containerSettings

    moduleWithNewSettings(allSettings.toSet)
  }

  private def resolverSettingsSet(
      dependencies: Set[JvmModuleID]
  ): Set[Def.Setting[_]] = {
    val dependencyResolvers =
      dependencies.flatMap(dep => dep.resolverForThisModuleId)
    Set(resolvers ++= dependencyResolvers.toSeq)
  }

  private def moduleIdSettings(
      dependencies: Set[ModuleID]
  ): Set[Def.Setting[_]] = {
    Set(libraryDependencies ++= dependencies.toSeq)
  }

  def settings: Seq[Def.Setting[_]] = {
    this.sbtSettings.toSeq ++ Set(
      publishLocalConfiguration := publishLocalConfiguration.value
        .withOverwrite(true)
    )
  }
}

//object ModuleBuild {
//
//  def apply(
//      projectOrganization: String,
//      projectArtifact: String,
//      mavenVersion: String
//  ): ModuleBuild[_] = {
//    //user shouldn't be forced to set this variable
//    val sbtOfflineMode = sys.env.getOrElse("SBT_OFFLINE_MODE", "false").toBoolean
//
//    val defaultSettings: Set[Def.Setting[_]] = Set(
//      name := projectArtifact,
//      version := mavenVersion,
//      organization := projectOrganization,
//      offline := sbtOfflineMode
//    )
//    new ModuleBuild[_](defaultSettings)
//  }
//}
