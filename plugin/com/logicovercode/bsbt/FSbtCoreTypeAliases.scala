package com.logicovercode.bsbt

import sbt._

trait FSbtCoreTypeAliases {

  type DockerNetwork = com.logicovercode.wdocker.DockerNetwork
  val DockerNetwork = com.logicovercode.wdocker.DockerNetwork

  def fsbt_adts() : ModuleID = {
    "com.logicovercode" %% "fsbt-adts" % "0.0.001"
  }

  def docker_core() : ModuleID = {
    "com.logicovercode" %% "docker-core" % "0.0.004"
  }
}
