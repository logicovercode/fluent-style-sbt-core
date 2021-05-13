package org.logicovercode.bsbt.docker.model

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.core.DefaultDockerClientConfig
import com.github.dockerjava.netty.NettyDockerCmdExecFactory
import com.whisk.docker.impl.dockerjava.{Docker, DockerJavaExecutor, DockerJavaExecutorFactory}
import com.whisk.docker.{DockerCommandExecutor, DockerFactory}
import org.logicovercode.bsbt.docker.win.DockerHostAndClientReResolver
import org.logicovercode.bsbt.os.OsFunctions.{currentOsOption, isWindowsCategoryOs}

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
        println("creating windows style docker instance")
        val _ @(host, client) = DockerHostAndClientReResolver.hostAndClient()
        val dockerFactory = new DockerFactory {
          override def createExecutor(): DockerCommandExecutor = new DockerJavaExecutor(host, client)
        }
        (dockerFactory, client)
      case false =>
        println("creating linux style docker instance")
        val docker = new Docker(DefaultDockerClientConfig.createDefaultConfigBuilder().build(), factory = new NettyDockerCmdExecFactory())
        (new DockerJavaExecutorFactory(docker), docker.client)
    }
  }
}
