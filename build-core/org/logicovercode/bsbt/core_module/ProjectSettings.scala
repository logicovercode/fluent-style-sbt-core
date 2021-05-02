package org.logicovercode.bsbt.core_module

import sbt._

trait ProjectSettings {

  implicit class ProjectExtension(project: Project) {
    def dependsOn(sbtModules: SbtModule*): Project = {
      val _ @(sbtModulesWithSourcesRequired, sbtModulesWithSourceNotRequired) =
        sbtModules.partition(_.includeSource)
      val moduleIds = sbtModulesWithSourceNotRequired.map(_.moduleID)
      val classPathProjectRefs =
        sbtModulesWithSourcesRequired.map(tryToGetModuleSource(_))
      project
        .settings(Keys.libraryDependencies ++= moduleIds)
        .dependsOn(classPathProjectRefs: _*)
    }
  }

  private def tryToGetModuleSource(
      sbtModule: SbtModule
  ): ClasspathDep[ProjectReference] = {
    sbtModule.optionalSource match {
      case Some(projectRef) => projectRef
      case None =>
        throw new RuntimeException(
          "module source not defined for " + sbtModule.moduleID
        )
    }
  }

  val Module = model.SbtModule
  type Module = model.SbtModule

  @Deprecated
  val SbtModule = model.SbtModule
  @Deprecated
  type SbtModule = model.SbtModule
}
