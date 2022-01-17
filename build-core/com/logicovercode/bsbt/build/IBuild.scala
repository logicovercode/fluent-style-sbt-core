package com.logicovercode.bsbt.build

import com.logicovercode.bsbt.module_id.JvmModuleID
import com.logicovercode.fsbt.commons.SbtMicroservice
import sbt.{Def, ModuleID}

trait ISettings{
  def settings: Seq[Def.Setting[_]]
}

trait IBuild[T <: Build[T]] extends ISettings{

  def sourceDirectories(projectSourceDirectories: String*): T

  def resourceDirectories(projectResourceDirectories: String*): T

  def testSourceDirectories(testSourceDirectories: String*): T

  def testResourceDirectories(testResourceDirectories: String*): T

  def sbtPlugins(sbtPlugins: ModuleID*): T

  def dependencies(projectDependencies: Seq[JvmModuleID]*): T

  def testDependencies(projectDependencies: Seq[JvmModuleID]*): T

  def services(dockerServices: SbtMicroservice*): T

  def javaCompatibility(source : String, target : String) : T
}
