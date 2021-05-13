package org.logicovercode.bsbt.docker.utils

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.netty.NettyDockerCmdExecFactory
import com.whisk.docker.{DockerCommandExecutor, DockerFactory}
import com.whisk.docker.impl.dockerjava.{Docker, DockerJavaExecutor, DockerJavaExecutorFactory}
import org.logicovercode.bsbt.docker.model.{ServiceDescription, MicroService}
import org.logicovercode.bsbt.docker.win.DockerHostAndClientReResolver
import org.logicovercode.bsbt.os.OsFunctions.isWindowsCategoryOs

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

    import org.logicovercode.bsbt.os.OsFunctions.isWindowsCategoryOs

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

    val allContainerNetworksInSingleService = dockerService.serviceDescriptions.map(_.container.networkMode)

    val optionComparer = Ordering[Option[String]]

    val allContainersNetworksAreSame = {
      allContainerNetworksInSingleService.size == dockerService.serviceDescriptions.size &&
      allContainerNetworksInSingleService.forall(netOpt => optionComparer.compare(netOpt, allContainerNetworksInSingleService(0)) == 0)
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

  private def triggerService(dockerService: MicroService): Unit = {

    dockerService.serviceDescriptions.foreach { dockerImage =>

      val imageName = dockerImage.container.image
      println(s"pulling image >$imageName<")

      import scala.sys.process._
      val dockerPullCommand = s"docker pull $imageName"
      println(s"$dockerPullCommand")
      s"$dockerPullCommand" !

      println("starting container from image : " + imageName)
      dockerImage.startAllOrFail()
    }
  }

  def createNetworkIfNotExists(networkName: String, dockerClient: DockerClient): Unit = {
    val networks = dockerClient.listNetworksCmd.withNameFilter(networkName).exec
    if (networks.isEmpty) {
      val networkResponse = dockerClient.createNetworkCmd.withName(networkName).withAttachable(true).withDriver("bridge").exec
      println(s"Network $networkName created with id ${networkResponse.getId}\n")
    }
  }
}
