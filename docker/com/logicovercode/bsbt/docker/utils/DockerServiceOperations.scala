package com.logicovercode.bsbt.docker.utils

import com.logicovercode.bsbt.docker.model.{DockerInfra, MicroService}
import com.logicovercode.wdocker.DockerSystem

object DockerServiceOperations {

  def startService(dockerService: MicroService): Unit = {

    val allContainerNetworksInSingleService = dockerService.serviceDescriptions.map(_.container.networkMode).flatten

    val allContainersNetworksAreSame = {
      allContainerNetworksInSingleService.size == dockerService.serviceDescriptions.size &&
      allContainerNetworksInSingleService.forall(_ == allContainerNetworksInSingleService.head)
    }

    dockerService.serviceDescriptions.size == 0 match {
      case true => println("no services defined to start ...")
      case false => {
        allContainersNetworksAreSame match {
          case true  => triggerService(dockerService)
          case false => println(s"networks don't match for $dockerService")
        }
      }
    }
  }

  private def triggerService(microService: MicroService): Unit = {

    microService.serviceDescriptions.foreach { serviceDescription =>
      val imageName = serviceDescription.container.image
      if (serviceDescription.useRemoteImage) {
        println(s"pulling image >$imageName<, this might take some time ...")
        DockerSystem.pullDockerImage(imageName)(DockerInfra.dockerClient)
      } else {
        println(s"using local image $imageName")
      }

      println("now image is pulled and container will be started for image : " + imageName)
      serviceDescription.startAllOrFail()
    }
  }
}
