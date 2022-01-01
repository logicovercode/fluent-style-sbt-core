package com.logicovercode.bsbt.build

import com.logicovercode.bsbt.docker.DockerSettings
import com.logicovercode.bsbt.docker.service.MicroService
import com.logicovercode.bsbt.module_id.JvmModuleID
import sbt.Keys._
import sbt._

trait Build[T <: Build[T]]
    extends IBuild[T]
    //with BuildFactory[T]
    with DockerSettings {

  val sbtSettings: Set[Def.Setting[_]]

  def moduleWithNewSettings(sbtSettings: Set[Def.Setting[_]]): T

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

    val jvmModuleIdsWithoutConfig = jvmModuleIdSet.map{ jvmModuleId =>
      val moduleId = jvmModuleId.moduleID
      val moduleIdWithoutConfig = moduleId.withConfigurations(None)
      JvmModuleID( moduleIdWithoutConfig )
    }

    moduleWithNewSettings(
      this.sbtSettings ++ moduleIdSettings(
        jvmModuleIdsWithoutConfig.map(_.moduleID % Test)
      ) ++ resolverSettingsSet(jvmModuleIdsWithoutConfig)
    )
  }

  def services(dockerServices: MicroService*): T = {
    //this will remove duplicates
    val serviceSettings: Seq[Def.Setting[_]] = Seq(
      dependentServices := dockerServices.toSet
    )

    val allAdditionalSettings = for {
      dockerService <- dockerServices
      serviceDescription <- dockerService.sbtServiceDescriptions
      settings <- serviceDescription.serviceAdditionalSettings
    } yield settings

    //val containerSettings = dockerServices.flatMap(_.sbtSettings())

    val allSettings = this.sbtSettings ++ serviceSettings ++ allAdditionalSettings

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
