package org.logicovercode.bsbt.docker.model

import com.whisk.docker.{DockerContainer, DockerFactory, DockerKit}
import sbt._

import scala.concurrent.duration.FiniteDuration

trait IDockerService {
  def instance(): DockerService
  def sbtSettings(): Set[Def.Setting[_]]
}

case class DockerService(containerDefinitions : Seq[DockerContainerDefinition])

case class DockerKitPerContainer(dockerContainer: DockerContainer,
                                 imagePullTimeout : FiniteDuration,
                                 containerStartTimeout : FiniteDuration)(implicit javaDockerFactory: DockerFactory) extends DockerKit {

  override val StartContainersTimeout = containerStartTimeout
  override val PullImagesTimeout = imagePullTimeout
  override implicit def dockerFactory: DockerFactory = javaDockerFactory
  override def dockerContainers: List[DockerContainer] = List(dockerContainer)
}
