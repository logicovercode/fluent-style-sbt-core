package com.logicovercode.bsbt

import better.files.File
import com.logicovercode.bsbt.sbt_module.SbtProject
import com.logicovercode.wdocker.OsFunctions.isWindowsCategoryOs
import org.apache.ivy.core.module.descriptor.License
import sbt._

trait FSbtCoreTypeAliases {

  val scala_2_13_MaxVersion = "2.13.7"
  val scala_2_12_MaxVersion = "2.12.15"

  type DockerNetwork = com.logicovercode.wdocker.DockerNetwork
  val DockerNetwork = com.logicovercode.wdocker.DockerNetwork

  val SbtModule = SbtProject

  val HOME = isWindowsCategoryOs() match {
    case true => File( sys.env("USERPROFILE") )
    case false    => File( sys.env("HOME") )
  }

  val PARENT_DIRECTORY = File("..")

  def fsbt_adts() : ModuleID = {
    "com.logicovercode" %% "fsbt-adts" % "0.0.001"
  }

  def docker_core() : ModuleID = {
    "com.logicovercode" %% "docker-core" % "0.0.004"
  }

  val Apache_2_0_License = new License("Apache-2.0", "http://www.apache.org/licenses/LICENSE-2.0")
  val MIT_License = new License("MIT", "https://opensource.org/licenses/MIT")

  val typesafe = "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases"
  val cloudera = "Cloudera Rel Repository" at "https://repository.cloudera.com/content/repositories/releases"
  val maven = "Maven Repository" at "https://repo1.maven.org/maven2"
  val springMileStones = "Spring Milestones" at "https://repo.spring.io/milestone"



}
