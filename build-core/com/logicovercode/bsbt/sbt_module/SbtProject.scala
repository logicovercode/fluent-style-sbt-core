package com.logicovercode.bsbt.sbt_module

import better.files.File
import sbt.{ClasspathDep, ModuleID, ProjectRef, ProjectReference, classpathDependency, uri}

case class SbtProject private[SbtProject](
    moduleID: ModuleID,
    //TODO, this field is added, as not able to extract ProjectRef from ClasspathDep[ProjectReference]
    private val optionalProjectRef: Option[ProjectRef],
    private val optionalModuleSource: Option[ClasspathDep[ProjectReference]],
    private val includeModuleSource: Boolean = false
) {

  def sourceCode: SbtProject =
    SbtProject(moduleID, optionalProjectRef, optionalModuleSource, true)

  def projectRef: ProjectRef = optionalProjectRef.get

  //TODO : restrict access to below methods
  //with in this project only
  def includeSource = includeModuleSource
  def optionalSource = optionalModuleSource
}

object SbtProject {

  def apply(
      moduleID: ModuleID,
      sbtModulePath: File,
      sbtModuleName: String
  ): SbtProject = {
    val cpd = ProjectRef(uri(sbtModulePath.toJava.getAbsolutePath), sbtModuleName)
    new SbtProject(moduleID, Option(cpd), Option(classpathDependency(cpd)))
  }

  def apply(moduleID: ModuleID): SbtProject = {
    new SbtProject(moduleID, None, None)
  }
}
