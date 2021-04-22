package org.logicovercode.bsbt.core.model

import sbt.{ClasspathDep, ModuleID, ProjectRef, ProjectReference, classpathDependency, uri}

case class SbtModule private[SbtModule] (
    moduleID: ModuleID,
    //TODO, this field is added, as not able to extract ProjectRef from ClasspathDep[ProjectReference]
    private val optionalProjectRef: Option[ProjectRef],
    private val optionalModuleSource: Option[ClasspathDep[ProjectReference]],
    private val includeModuleSource: Boolean = false
) {

  def sourceCode: SbtModule =
    SbtModule(moduleID, optionalProjectRef, optionalModuleSource, true)

  def projectRef: ProjectRef = optionalProjectRef.get

  //TODO : restrict access to below methods
  //with in this project only
  def includeSource = includeModuleSource
  def optionalSource = optionalModuleSource
}

object SbtModule {

  def apply(
      moduleID: ModuleID,
      sbtModulePath: String,
      sbtModuleName: String
  ): SbtModule = {
    val cpd = ProjectRef(uri(sbtModulePath), sbtModuleName)
    new SbtModule(moduleID, Option(cpd), Option(classpathDependency(cpd)))
  }

  def apply(moduleID: ModuleID): SbtModule = {
    new SbtModule(moduleID, None, None)
  }
}
