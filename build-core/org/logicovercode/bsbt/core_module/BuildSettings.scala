package org.logicovercode.bsbt.core_module

import org.apache.ivy.core.module.descriptor.License
import org.logicovercode.bsbt.docker.DockerSettings
import org.logicovercode.bsbt.docker.model.IDockerService
import org.logicovercode.bsbt.module_id.JvmModuleID
import sbt.Keys._
import sbt._
import sbt.librarymanagement.{Developer, MavenRepository, ScmInfo}

import java.net.URL

abstract class BuildSettings[T <: BuildSettings[T]] ( val sbtSettings: Set[Def.Setting[_]])
  extends IBuildSettings[T]
    with BuildFactory[T]
    with DockerSettings {

  def sbtOffLineMode(sbtOffLineMode: Boolean): T = {
    val _settings = Set(
      offline := sbtOffLineMode
    )
    moduleWithNewSettings(this.sbtSettings ++ _settings)
  }

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

  def argsRequiredForPublishing(
                                 projectDevelopers: List[Developer],
                                 license: License,
                                 homePageUrl: URL,
                                 moduleScmInfo: ScmInfo,
                                 mavenRepository: MavenRepository
                               ): T = {

    println("adding publishing settings")
    val _settings = Set(
      licenses += (license.getName, new URL(license.getUrl)),
      homepage := Option(homePageUrl),
      scmInfo := Option(moduleScmInfo),
      developers := projectDevelopers,
      publishMavenStyle := true,
      publishTo := Some(mavenRepository)
    )

    val allSettings = this.sbtSettings ++ _settings
    moduleWithNewSettings(allSettings)
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
