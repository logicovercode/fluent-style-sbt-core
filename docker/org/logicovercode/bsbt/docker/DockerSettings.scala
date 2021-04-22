package org.logicovercode.bsbt.docker

import org.logicovercode.bsbt.docker.utils.DockerCommandOperations._
import org.logicovercode.bsbt.docker.model.{DockerContainerDefinition, DockerService}
import org.logicovercode.bsbt.docker.utils.DockerUtils
import org.logicovercode.bsbt.docker.utils.DockerUtils.createNetworkIfNotExists
import org.logicovercode.bsbt.os.OsFunctions.currentOsOption
import sbt.Keys._
import sbt.{Def, _}

trait DockerSettings  {

  lazy val dependentDockerServices =
    settingKey[Set[DockerService]]("set of dependent docker services")
  lazy val startServices = taskKey[Unit]("start dependent containers")
  lazy val buildImage =
    inputKey[Unit]("build docker image on local with latest tag")
  lazy val tagImageForGitHub =
    taskKey[Unit]("tag latest docker image to push on github")
  lazy val publishImageToGitHub = taskKey[Unit]("publish image to github")

  lazy val dockerSettings = Seq[Def.Setting[_]](
    dependentDockerServices := Set(),
    startServices := {
      val dockerServices = (dependentDockerServices.value)
      val (dockerFactory, dockerClient) = DockerUtils.dockerFactoryAndClient( currentOsOption )

      val allNetworks = (for{
        dockerService <- dockerServices
        containerDefinition <- dockerService.containerDefinitions
        network = containerDefinition.dockerContainer.networkMode
      } yield network).flatten

      allNetworks.foreach( DockerUtils.createNetworkIfNotExists(_, dockerClient) )
      dockerServices.par.foreach( DockerUtils.startService(_, dockerFactory) )

      val sbtProcessId = DockerUtils.pid()
      DockerUtils.killDockerManager(sbtProcessId, currentOsOption)
    },
    buildImage := {
      import complete.DefaultParsers._
      val args = spaceDelimited("").parsed
      val _ @(executionDir, dockerArgs, dockerFile) = parseDockerCommandArgs(args)
      val dockerImageName = imageName(organization.value, name.value)
      buildDockerImageWithLatestTag(executionDir, dockerArgs, dockerFile, dockerImageName)

      val tag = s"$dockerImageName:${version.value}"
      tagLatestDockerImageWithVersionTag(dockerImageName, tag)
    },
    tagImageForGitHub := {
      val gitUser = sys.env.get("REPO_GITHUB_USER")

      gitUser match {
        case Some(user) =>
          val dockerImageName = imageName(organization.value, name.value)
          val githubTag = s"ghcr.io/$user/${name.value}:${version.value}"
          tagLatestDockerImageWithVersionTag(dockerImageName, githubTag)

        case None =>
          println(s"environment variable [REPO_GITHUB_USER] is not set")
      }
    },
    publishImageToGitHub := {

      val gitUser = sys.env.get("REPO_GITHUB_USER")

      gitUser match {
        case Some(user) =>
          val githubTag = s"ghcr.io/$user/${name.value}:${version.value}"
          publishDockerImage(githubTag)
        case None =>
          println(s"environment variable [REPO_GITHUB_USER] is not set")
      }
    }
  )
}
