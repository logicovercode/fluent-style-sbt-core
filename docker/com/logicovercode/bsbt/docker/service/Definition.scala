package com.logicovercode.bsbt.docker.service

import com.logicovercode.wdocker.{ContainerDefinition, DockerNetwork}
import sbt.Def

import scala.concurrent.duration.FiniteDuration

case class MicroService(sbtServiceDescriptions : Seq[SbtServiceDescription]){

  def networks() : Seq[DockerNetwork] = {
    (for{
      serviceDescription <- sbtServiceDescriptions
      network = serviceDescription.container.networkMode
    } yield network).flatten
  }
}

case class SbtServiceDescription(container: ContainerDefinition,
                                 serviceAdditionalSettings: Set[Def.Setting[_]],
                                 imagePullTimeout : FiniteDuration,
                                 containerStartTimeout : FiniteDuration)
