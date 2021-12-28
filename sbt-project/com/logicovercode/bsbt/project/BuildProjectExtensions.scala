package com.logicovercode.bsbt.project

import com.logicovercode.bsbt.sbt_module.SbtProject
import sbt.{ClasspathDep, Keys, Project, ProjectReference}

trait BuildProjectExtensions {

  implicit class ProjectExtension(project: Project) {
    def dependsOn(sbtModules: SbtProject*): Project = {
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
      sbtModule: SbtProject
  ): ClasspathDep[ProjectReference] = {
    sbtModule.optionalSource match {
      case Some(projectRef) => projectRef
      case None =>
        throw new RuntimeException(
          "module source not defined for " + sbtModule.moduleID
        )
    }
  }
}
