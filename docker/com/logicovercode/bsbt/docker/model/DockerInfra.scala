package com.logicovercode.bsbt.docker.model

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.netty.NettyDockerCmdExecFactory
import com.logicovercode.wdocker.OsFunctions.{currentOsOption, isWindowsCategoryOs}
import com.logicovercode.wdocker.{Docker, DockerCommandExecutor, DockerFactory, DockerHostAndClientReResolver, DockerJavaExecutor, DockerJavaExecutorFactory}

trait DockerInfra{
  implicit final val dockerFactory = DockerInfra.javaDockerFactory
}

object DockerInfra {

  private val dockerFactoryClientTuple = dockerFactoryAndClient(currentOsOption)
  val javaDockerFactory = dockerFactoryClientTuple._1
  val dockerClient = dockerFactoryClientTuple._2

  private def dockerFactoryAndClient(osOption: Option[String]): (DockerFactory, DockerClient) = {

    isWindowsCategoryOs(osOption) match {
      case true =>
        val _ @(host, client) = DockerHostAndClientReResolver.hostAndClient()
        val dockerFactory = new DockerFactory {
          override def createExecutor(): DockerCommandExecutor = new DockerJavaExecutor(host, client)
        }
        (dockerFactory, client)
      case false =>
        val docker = new Docker(DefaultDockerClientConfig.createDefaultConfigBuilder().build(), factory = new NettyDockerCmdExecFactory())
        (new DockerJavaExecutorFactory(docker), docker.client)
    }
  }
}
