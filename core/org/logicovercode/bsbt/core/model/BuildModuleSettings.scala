package org.logicovercode.bsbt.core.model

import org.logicovercode.bsbt.docker.model.IDockerService
import sbt.{Def, ModuleID}

trait BuildModuleSettings[T] {

  def sbtOffLineMode(sbtOffLineMode: Boolean): T

  def sourceDirectories(projectSourceDirectories: String*): T

  def resourceDirectories(projectResourceDirectories: String*): T

  def testSourceDirectories(testSourceDirectories: String*): T

  def testResourceDirectories(testResourceDirectories: String*): T

  def sbtPlugins(sbtPlugins: ModuleID*): T

  def moduleScalaVersion(scalaVersion: String): T

  def dependencies(projectDependencies: Seq[JvmModuleID]*): T

  def testDependencies(projectDependencies: Seq[JvmModuleID]*): T

  def dockerServices(dockerServices: IDockerService*): T

  def settings: Seq[Def.Setting[_]]
}
