package org.logicovercode.bsbt.docker

import org.logicovercode.bsbt.docker.model._

trait DockerImplicitConversions {

  implicit def dockerImageToIDockerService(serviceDescription: ServiceDescription): MicroService = {
    MicroService(Seq(serviceDescription))

  }

  implicit def serviceDescriptionsTupleToIDockerService(serviceDescriptions: (ServiceDescription, ServiceDescription)): MicroService = {
    MicroService(Seq(serviceDescriptions._1, serviceDescriptions._2))
  }

  implicit def serviceDescriptionsTupleToIDockerService(serviceDescriptions: (ServiceDescription, ServiceDescription, ServiceDescription)): MicroService = {
    MicroService(Seq(serviceDescriptions._1, serviceDescriptions._2, serviceDescriptions._3))
  }

  implicit def serviceDescriptionsTupleToIDockerService(serviceDescriptions: (ServiceDescription, ServiceDescription, ServiceDescription, ServiceDescription)): MicroService = {
    MicroService(
      Seq(serviceDescriptions._1, serviceDescriptions._2, serviceDescriptions._3, serviceDescriptions._4)
    )
  }

  implicit def serviceDescriptionsTupleToIDockerService(serviceDescriptions: (ServiceDescription, ServiceDescription, ServiceDescription, ServiceDescription, ServiceDescription)): MicroService = {
    MicroService(
      Seq(serviceDescriptions._1, serviceDescriptions._2, serviceDescriptions._3, serviceDescriptions._4, serviceDescriptions._5)
    )
  }

  implicit def serviceDescriptionsTupleToIDockerService(serviceDescriptions: (ServiceDescription, ServiceDescription, ServiceDescription, ServiceDescription, ServiceDescription, ServiceDescription)): MicroService = {
    MicroService(
      Seq(serviceDescriptions._1, serviceDescriptions._2, serviceDescriptions._3, serviceDescriptions._4, serviceDescriptions._5, serviceDescriptions._6)
    )
  }

  implicit def serviceDescriptionsTupleToIDockerService(serviceDescriptions: (ServiceDescription, ServiceDescription, ServiceDescription, ServiceDescription, ServiceDescription, ServiceDescription, ServiceDescription)): MicroService = {
    MicroService(
      Seq(serviceDescriptions._1, serviceDescriptions._2, serviceDescriptions._3, serviceDescriptions._4, serviceDescriptions._5, serviceDescriptions._6, serviceDescriptions._7)
    )
  }
}