package com.logicovercode.bsbt.docker

import com.logicovercode.bsbt.docker.service.{MicroService, SbtServiceDescription}

trait DockerImplicitConversions {

  type DockerNetwork = com.logicovercode.wdocker.DockerNetwork
  val DockerNetwork = com.logicovercode.wdocker.DockerNetwork

  implicit def dockerImageToIDockerService(serviceDescription: SbtServiceDescription): MicroService = {
    MicroService(Seq(serviceDescription))
  }

  implicit def serviceDescriptionsTupleToIDockerService(serviceDescriptions: (SbtServiceDescription, SbtServiceDescription)): MicroService = {
    MicroService(Seq(serviceDescriptions._1, serviceDescriptions._2))
  }

  implicit def serviceDescriptionsTupleToIDockerService(serviceDescriptions: (SbtServiceDescription, SbtServiceDescription, SbtServiceDescription)): MicroService = {
    service.MicroService(Seq(serviceDescriptions._1, serviceDescriptions._2, serviceDescriptions._3))
  }

  implicit def serviceDescriptionsTupleToIDockerService(serviceDescriptions: (SbtServiceDescription, SbtServiceDescription, SbtServiceDescription, SbtServiceDescription)): MicroService = {
    service.MicroService(
      Seq(serviceDescriptions._1, serviceDescriptions._2, serviceDescriptions._3, serviceDescriptions._4)
    )
  }

  implicit def serviceDescriptionsTupleToIDockerService(serviceDescriptions: (SbtServiceDescription, SbtServiceDescription, SbtServiceDescription, SbtServiceDescription, SbtServiceDescription)): MicroService = {
    service.MicroService(
      Seq(serviceDescriptions._1, serviceDescriptions._2, serviceDescriptions._3, serviceDescriptions._4, serviceDescriptions._5)
    )
  }

  implicit def serviceDescriptionsTupleToIDockerService(serviceDescriptions: (SbtServiceDescription, SbtServiceDescription, SbtServiceDescription, SbtServiceDescription, SbtServiceDescription, SbtServiceDescription)): MicroService = {
    service.MicroService(
      Seq(serviceDescriptions._1, serviceDescriptions._2, serviceDescriptions._3, serviceDescriptions._4, serviceDescriptions._5, serviceDescriptions._6)
    )
  }

  implicit def serviceDescriptionsTupleToIDockerService(serviceDescriptions: (SbtServiceDescription, SbtServiceDescription, SbtServiceDescription, SbtServiceDescription, SbtServiceDescription, SbtServiceDescription, SbtServiceDescription)): MicroService = {
    service.MicroService(
      Seq(serviceDescriptions._1, serviceDescriptions._2, serviceDescriptions._3, serviceDescriptions._4, serviceDescriptions._5, serviceDescriptions._6, serviceDescriptions._7)
    )
  }
}