package org.logicovercode.bsbt.build

import org.logicovercode.bsbt.docker.DockerSettings
import org.logicovercode.bsbt.docker.model.IDockerService
import org.logicovercode.bsbt.module_id.JvmModuleID
import sbt.Keys._
import sbt._

trait Build[T <: Build[T]]
    extends IBuild[T]
    //with BuildFactory[T]
    with DockerSettings {

  val sbtSettings: Set[Def.Setting[_]]

  def moduleWithNewSettings(sbtSettings: Set[Def.Setting[_]]) : T

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
      ) ++ resolverSettingsSet(jvmModuleIdSet)
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
    val dockerServiceSettings: Seq[Def.Setting[_]] = Seq(
      dependentDockerServices := dockerInstancesSet
    )

    val containerSettings = dockerServices.flatMap(_.sbtSettings())

    val allSettings = this.sbtSettings ++ dockerServiceSettings ++ containerSettings

    moduleWithNewSettings(allSettings)
  }

  protected def resolverSettingsSet(
      dependencies: Set[JvmModuleID]
  ): Set[Def.Setting[_]] = {
    val dependencyResolvers =
      dependencies.flatMap(dep => dep.resolverForThisModuleId)
    Set(resolvers ++= dependencyResolvers.toSeq)
  }

  protected def moduleIdSettings(
      dependencies: Set[ModuleID]
  ): Set[Def.Setting[_]] = {
    Set(libraryDependencies ++= dependencies.toSeq)
  }

  def settings: Seq[Def.Setting[_]] = {
    this.sbtSettings.toSeq
  }
}
