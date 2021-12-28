package com.logicovercode.bsbt.docker

import com.logicovercode.bsbt.docker.model.{MicroService, ServiceDescription}
import com.logicovercode.bsbt.docker.model._

trait DockerImplicitConversions {

  type DockerNetwork = com.logicovercode.wdocker.DockerNetwork
  val DockerNetwork = com.logicovercode.wdocker.DockerNetwork

  implicit def dockerImageToIDockerService(serviceDescription: ServiceDescription): MicroService = {
    MicroService(Seq(serviceDescription))
  }

  implicit def serviceDescriptionsTupleToIDockerService(serviceDescriptions: (ServiceDescription, ServiceDescription)): MicroService = {
    model.MicroService(Seq(serviceDescriptions._1, serviceDescriptions._2))
  }

  implicit def serviceDescriptionsTupleToIDockerService(serviceDescriptions: (ServiceDescription, ServiceDescription, ServiceDescription)): MicroService = {
    model.MicroService(Seq(serviceDescriptions._1, serviceDescriptions._2, serviceDescriptions._3))
  }

  implicit def serviceDescriptionsTupleToIDockerService(serviceDescriptions: (ServiceDescription, ServiceDescription, ServiceDescription, ServiceDescription)): MicroService = {
    model.MicroService(
      Seq(serviceDescriptions._1, serviceDescriptions._2, serviceDescriptions._3, serviceDescriptions._4)
    )
  }

  implicit def serviceDescriptionsTupleToIDockerService(serviceDescriptions: (ServiceDescription, ServiceDescription, ServiceDescription, ServiceDescription, ServiceDescription)): MicroService = {
    model.MicroService(
      Seq(serviceDescriptions._1, serviceDescriptions._2, serviceDescriptions._3, serviceDescriptions._4, serviceDescriptions._5)
    )
  }

  implicit def serviceDescriptionsTupleToIDockerService(serviceDescriptions: (ServiceDescription, ServiceDescription, ServiceDescription, ServiceDescription, ServiceDescription, ServiceDescription)): MicroService = {
    model.MicroService(
      Seq(serviceDescriptions._1, serviceDescriptions._2, serviceDescriptions._3, serviceDescriptions._4, serviceDescriptions._5, serviceDescriptions._6)
    )
  }

  implicit def serviceDescriptionsTupleToIDockerService(serviceDescriptions: (ServiceDescription, ServiceDescription, ServiceDescription, ServiceDescription, ServiceDescription, ServiceDescription, ServiceDescription)): MicroService = {
    model.MicroService(
      Seq(serviceDescriptions._1, serviceDescriptions._2, serviceDescriptions._3, serviceDescriptions._4, serviceDescriptions._5, serviceDescriptions._6, serviceDescriptions._7)
    )
  }
}