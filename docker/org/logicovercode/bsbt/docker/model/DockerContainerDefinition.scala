package org.logicovercode.bsbt.docker.model

import com.whisk.docker._

import scala.concurrent.duration.FiniteDuration

case class DockerContainerDefinition(dockerContainer: DockerContainer, pullTimeout: FiniteDuration, startTimeout: FiniteDuration) {

  def withCommand(cmd: String*): DockerContainerDefinition = {
    val newDockerContainer = dockerContainer.copy(command = Some(cmd))
    copy(dockerContainer = newDockerContainer)
  }

  def withEntrypoint(entrypoint: String*): DockerContainerDefinition = {
    val newDockerContainer = dockerContainer.copy(entrypoint = Some(entrypoint))
    copy(dockerContainer = newDockerContainer)
  }

  def withPorts(ps: (Int, Option[Int])*): DockerContainerDefinition = {
    val newDockerContainer = dockerContainer.copy(bindPorts = ps.map { case (internalPort, hostPort) =>
      internalPort -> DockerPortMapping(hostPort)
    }.toMap)
    copy(dockerContainer = newDockerContainer)
  }

  def withReadyChecker(checker: DockerReadyChecker) :DockerContainerDefinition = {
    val newDockerContainer = dockerContainer.copy(readyChecker = checker)
    copy(dockerContainer = newDockerContainer)
  }

  def withEnv(env: String*): DockerContainerDefinition = {
    val newDockerContainer = dockerContainer.copy(env = env)
    copy(dockerContainer = newDockerContainer)
  }

  def withNetworkMode(networkMode: String) : DockerContainerDefinition = {
    val newDockerContainer = dockerContainer.copy(networkMode = Some(networkMode))
    copy(dockerContainer = newDockerContainer)

  }

  def withVolumes(volumeMappings: Seq[VolumeMapping]) : DockerContainerDefinition = {
    val newDockerContainer = dockerContainer.copy(volumeMappings = volumeMappings)
    copy(dockerContainer = newDockerContainer)
  }

  def withLogLineReceiver(logLineReceiver: LogLineReceiver) : DockerContainerDefinition = {
    val newDockerContainer = dockerContainer.copy(logLineReceiver = Some(logLineReceiver))
    copy(dockerContainer = newDockerContainer)
  }
}