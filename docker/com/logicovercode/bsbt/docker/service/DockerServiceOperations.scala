package com.logicovercode.bsbt.docker.service

import com.github.dockerjava.api.DockerClient
import com.logicovercode.wdocker.DockerFactory
import com.logicovercode.wdocker.api.DockerSystem
import com.logicovercode.wdocker.api.DockerSystem.NetworkCreationFailed

import scala.util.{Failure, Success}

object DockerServiceOperations {

  def tryToStartService(microService: MicroService)(implicit dockerClient: DockerClient, dockerFactory: DockerFactory): Unit = {

//    val allContainerNetworksInSingleService = microService.serviceDescriptions.map(_.container.networkMode).flatten
//
//    val allContainersNetworksAreSame = {
//      allContainerNetworksInSingleService.size == microService.serviceDescriptions.size &&
//        allContainerNetworksInSingleService.forall(_ == allContainerNetworksInSingleService.head)
//    }

    val dockerNetworks = microService.networks()
    val serviceDescriptions = microService.sbtServiceDescriptions

    val networkStatusSeq = dockerNetworks.map(DockerSystem.tryNetworkConnectivity(_))
    val allNetworkExists = networkStatusSeq.forall(_.exists)

    if (allNetworkExists) {

      serviceDescriptions.foreach { serviceDescription =>
        DockerSystem.pullAndStartContainerDefinition(
          serviceDescription.container,
          serviceDescription.imagePullTimeout,
          serviceDescription.containerStartTimeout
        ) match {

          case Success(_) =>
            import serviceDescription.container._
            val img = mayBeHubUser.map(_ + "/").getOrElse("") + image + ":" + tag
            println(s"$img started successfully")

          case Failure(ex) => ex.printStackTrace()
        }
      }
    } else {
      val allErrors = networkStatusSeq collect { case NetworkCreationFailed(network, errorMsg) =>
        s"${network.name} does not exists. error >$errorMsg<"
      }
      throw new RuntimeException(allErrors.mkString("\n"))
    }
  }
}
