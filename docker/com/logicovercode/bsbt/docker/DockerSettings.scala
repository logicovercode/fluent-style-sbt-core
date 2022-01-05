package com.logicovercode.bsbt.docker

import com.logicovercode.bsbt.docker.cli.BuildImageMetaData
import com.logicovercode.bsbt.docker.cli.DockerCliOperations._
import com.logicovercode.bsbt.docker.service.DockerServiceOperations
import com.logicovercode.fsbt.commons.SbtMicroservice
import com.logicovercode.wdocker.OsFunctions.currentOsOption
import com.logicovercode.wdocker.api.{DockerContext, DockerProcessFunctions}
import sbt.Keys._
import sbt.{Def, _}

trait DockerSettings {

  lazy val dependentServices = settingKey[Set[SbtMicroservice]]("set of dependent docker services")
  lazy val startServices = taskKey[Unit]("start dependent containers")
  lazy val buildImage = inputKey[Unit]("build docker image on local with latest tag")
  lazy val buildImageForGitHub = inputKey[Unit]("build docker image on local with latest tag and github tag")

  lazy val dockerSettings = Seq[Def.Setting[_]](
    dependentServices := Set(),

    startServices := {
      val dockerServices = (dependentServices.value)

      dockerServices.par.foreach(DockerServiceOperations.tryToStartService(_)(DockerContext.dockerClient, DockerContext.dockerFactory))

      val sbtProcessId = DockerProcessFunctions.pid()
      DockerProcessFunctions.killDockerManager(sbtProcessId, currentOsOption)
    },

    buildImage := {
      import complete.DefaultParsers._
      val args = spaceDelimited("").parsed

      val buildImageMeta = prepareImageMeta(args, organization.value, name.value)
      buildDockerImage(buildImageMeta, "latest")
      tagDockerImage(buildImageMeta.imageName, "latest", version.value)
    },

    buildImageForGitHub := {
      import complete.DefaultParsers._
      val args = spaceDelimited("").parsed

      val gitUser = sys.env.get("TARGET_GITHUB_REPO")
      gitUser match {
        case Some(user) => {

          val buildImageMeta = prepareImageMeta(args, organization.value, name.value)

          val ghcrImageMeta = BuildImageMetaData(s"ghcr.io/${buildImageMeta.imageName}", buildImageMeta.executionDirectory,
            buildImageMeta.dockerFile, buildImageMeta.dockerArgs)

          buildDockerImage(ghcrImageMeta, "latest")
          tagDockerImage(ghcrImageMeta.imageName, "latest", version.value)
        }
        case None => println(s"environment variable [TARGET_GITHUB_REPO] is not set")
      }
    }
  )
}
