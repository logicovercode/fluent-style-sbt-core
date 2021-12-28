package com.logicovercode.bsbt.docker.utils

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.model.Network.Ipam
import com.github.dockerjava.api.model.Network.Ipam.Config
import com.logicovercode.bsbt.docker.model.MicroService
import com.logicovercode.wdocker.DockerNetwork

object DockerServiceOperations {
  def pid(): Long = {

    import java.lang.management.ManagementFactory
    val bean = ManagementFactory.getRuntimeMXBean

    // Get name representing the running Java virtual machine.
    // It returns something like 6460@AURORA. Where the value
    // before the @ symbol is the PID.
    val jvmName = bean.getName
    println("Name = " + jvmName)

    // Extract the PID by splitting the string returned by the
    // bean.getName() method.
    val pid = jvmName.split("@")(0).toLong
    println("PID  = " + pid)

    pid
  }

  def killDockerManager(
      sbtProcessId: Long,
      osNameOption: Option[String]
  ): Unit = {
    val msg =
      s"attempt to silent process(that is starting docker services) with pid  >$sbtProcessId< on ${osNameOption.get}"

    println(s"$msg")

    import com.logicovercode.bsbt.os.OsFunctions.isWindowsCategoryOs

    isWindowsCategoryOs(osNameOption) match {
      case false => killOnUnixBaseBox(sbtProcessId)
      case true  => killOnWindowsBox(sbtProcessId)
    }
  }

  private def killOnUnixBaseBox(processId: Long): Int = {
    println(s"kill -9 $processId")

    import scala.sys.process._

    s"kill -9 $processId" !
  }

  private def killOnWindowsBox(processId: Long): Int = {
    println(s"TASKKILL /F /PID $processId")

    import scala.sys.process._

    s"TASKKILL /F /PID $processId" !
  }

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
        println(s"pulling image >$imageName<")

        import scala.sys.process._
        val dockerPullCommand = s"docker pull $imageName"
        println(s"$dockerPullCommand")
        s"$dockerPullCommand" !
      } else {
        println(s"using local image $imageName")
      }

      println("now image is pulled and container will be started for image : " + imageName)
      serviceDescription.startAllOrFail()
    }
  }

  def createNetworkIfNotExists(network: DockerNetwork, dockerClient: DockerClient): Unit = {

    val baseNetworkCommand = dockerClient
      .createNetworkCmd()
      .withName(network.name)
      .withDriver("bridge")
      .withAttachable(true)

    val networkCommand = network.subnet match {
      case Some(sn) =>
        baseNetworkCommand.withIpam(
          new Ipam().withConfig(
            new Config()
              .withSubnet(sn)
          )
        )
      case None => baseNetworkCommand
    }

    val networkResponse = networkCommand.exec()
    println(s"Network ${network.name} created with id ${networkResponse.getId}\n")
  }
}
