package org.logicovercode.bsbt.core_module

import org.logicovercode.bsbt.docker.model.IDockerService
import org.logicovercode.bsbt.module_id.JvmModuleID
import sbt.{Def, ModuleID}

trait IBuildSettings[T <: BuildSettings[T]] {

  def sourceDirectories(projectSourceDirectories: String*): T

  def resourceDirectories(projectResourceDirectories: String*): T

  def testSourceDirectories(testSourceDirectories: String*): T

  def testResourceDirectories(testResourceDirectories: String*): T

  def sbtPlugins(sbtPlugins: ModuleID*): T

  def dependencies(projectDependencies: Seq[JvmModuleID]*): T

  def testDependencies(projectDependencies: Seq[JvmModuleID]*): T

  def dockerServices(dockerServices: IDockerService*): T

  def settings: Seq[Def.Setting[_]]
}
