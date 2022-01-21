package com.logicovercode.bsbt

import better.files.File
import com.logicovercode.bsbt.sbt_module.SbtProject
import com.logicovercode.wdocker.OsFunctions.isWindowsCategoryOs
import org.apache.ivy.core.module.descriptor.License
import sbt._

trait FSbtCoreTypeAliases {

  type DockerNetwork = com.logicovercode.wdocker.DockerNetwork
  val DockerNetwork = com.logicovercode.wdocker.DockerNetwork

  val SbtModule = SbtProject

  val HOME = isWindowsCategoryOs() match {
    case true => File( sys.env("USERPROFILE") )
    case false    => File( sys.env("HOME") )
  }

  val PARENT_DIRECTORY = File("..")

  val Apache_2_0_License = new License("Apache-2.0", "http://www.apache.org/licenses/LICENSE-2.0")
  val MIT_License = new License("MIT", "https://opensource.org/licenses/MIT")

  val typesafe = "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases"
  val cloudera = "Cloudera Rel Repository" at "https://repository.cloudera.com/content/repositories/releases"
  val maven = "Maven Repository" at "https://repo1.maven.org/maven2"
  val springMileStones = "Spring Milestones" at "https://repo.spring.io/milestone"
}
