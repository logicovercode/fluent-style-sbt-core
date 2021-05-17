package org.logicovercode.bsbt.docker.model

import com.whisk.docker._
import sbt.Def

import scala.concurrent.duration.FiniteDuration

case class MicroService(serviceDescriptions : Seq[ServiceDescription])

case class ServiceDescription(container: DockerContainer,
                              serviceAdditionalSettings: Set[Def.Setting[_]],
                              imagePullTimeout : FiniteDuration,
                              containerStartTimeout : FiniteDuration, useRemoteImage : Boolean = true)(implicit javaDockerFactory : DockerFactory) extends DockerKit {

  override val StartContainersTimeout = containerStartTimeout
  override val PullImagesTimeout = imagePullTimeout
  override implicit def dockerFactory: DockerFactory = javaDockerFactory
  override def dockerContainers: List[DockerContainer] = List(container)

  def withCommand(cmd: String*): ServiceDescription = {
    val newDockerContainer = container.copy(command = Some(cmd))
    copy(container = newDockerContainer)(javaDockerFactory)
  }

  def withEntrypoint(entrypoint: String*): ServiceDescription = {
    val newDockerContainer = container.copy(entrypoint = Some(entrypoint))
    copy(container = newDockerContainer)(javaDockerFactory)
  }

  def withPorts(ps: (Int, Option[Int])*): ServiceDescription = {
    val newDockerContainer = container.copy(bindPorts = ps.map { case (internalPort, hostPort) =>
      internalPort -> DockerPortMapping(hostPort)
    }.toMap)
    copy(container = newDockerContainer)(javaDockerFactory)
  }

  def withReadyChecker(checker: DockerReadyChecker) :ServiceDescription = {
    val newDockerContainer = container.copy(readyChecker = checker)
    copy(container = newDockerContainer)(javaDockerFactory)
  }

  def withEnv(env: String*): ServiceDescription = {
    val newDockerContainer = container.copy(env = env)
    copy(container = newDockerContainer)(javaDockerFactory)
  }

  def withNetworkMode(networkMode: String) : ServiceDescription = {
    val newDockerContainer = container.copy(networkMode = Some(networkMode))
    copy(container = newDockerContainer)(javaDockerFactory)

  }

  def withVolumes(volumeMappings: Seq[VolumeMapping]) : ServiceDescription = {
    val newDockerContainer = container.copy(volumeMappings = volumeMappings)
    copy(container = newDockerContainer)(javaDockerFactory)
  }

  def withLogLineReceiver(logLineReceiver: LogLineReceiver) : ServiceDescription = {
    val newDockerContainer = container.copy(logLineReceiver = Some(logLineReceiver))
    copy(container = newDockerContainer)(javaDockerFactory)
  }
}
