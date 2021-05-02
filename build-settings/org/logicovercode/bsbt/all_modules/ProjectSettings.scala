package org.logicovercode.bsbt.all_modules

import org.logicovercode.bsbt.core_module
import org.logicovercode.bsbt.java_module.JavaBuildSettings
import org.logicovercode.bsbt.scala_module.ScalaBuildSettings
import sbt.{ClasspathDep, Keys, Project, ProjectReference}

trait ProjectSettings {

  implicit class ProjectExtension(project: Project) {
    def dependsOn(sbtModules: Module*): Project = {
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
      sbtModule: Module
  ): ClasspathDep[ProjectReference] = {
    sbtModule.optionalSource match {
      case Some(projectRef) => projectRef
      case None =>
        throw new RuntimeException(
          "module source not defined for " + sbtModule.moduleID
        )
    }
  }

  val JBuild = JavaBuildSettings

  val SBuild = ScalaBuildSettings
  //type SBuild = ScalaBuildSettings

  val Module = core_module.SbtModule
  type Module = core_module.SbtModule

  @Deprecated
  val SbtModule = core_module.SbtModule
  @Deprecated
  type SbtModule = core_module.SbtModule
}
