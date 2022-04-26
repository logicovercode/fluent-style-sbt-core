package com.logicovercode.bsbt.build

import com.logicovercode.bsbt.module_id.JvmModuleID
import com.logicovercode.fsbt.commons.SbtService
import sbt.{Def, ModuleID}

trait ISettings{
  def settings: Seq[Def.Setting[_]]
}

sealed trait OutputVersion{
  def jSource() : String
  def jTarget() : String
  def sTarget() : String
  def sRelease() : String
}

trait IBuild[T <: Build[T]] extends ISettings{

  def sourceDirectories(projectSourceDirectories: String*): T

  def resourceDirectories(projectResourceDirectories: String*): T

  def testSourceDirectories(testSourceDirectories: String*): T

  def testResourceDirectories(testResourceDirectories: String*): T

  def sbtPlugins(sbtPlugins: ModuleID*): T

  def dependencies(projectDependencies: Seq[JvmModuleID]*): T

  def testDependencies(projectDependencies: Seq[JvmModuleID]*): T

  def services(dockerServices: SbtService*): T

  @Deprecated
  def javaCompatibility(source : String, target : String) : T

  def javaCompatibility(outputVersion: OutputVersion) : T
}

trait JavaOutputVersions{

  case object Jdk8 extends OutputVersion {
    override def jSource(): String = "8"

    override def jTarget(): String = "8"

    override def sTarget(): String = "8"

    override def sRelease(): String = "8"
  }

  case object Jdk11 extends OutputVersion {
    override def jSource(): String = "11"

    override def jTarget(): String = "11"

    override def sTarget(): String = "11"

    override def sRelease(): String = "11"
  }
}