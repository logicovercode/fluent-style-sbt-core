package com.logicovercode.bsbt.docker

import com.logicovercode.bsbt.docker.cli.BuildImageMetaData
import com.logicovercode.bsbt.docker.cli.DockerCliOperations._
import com.logicovercode.bsbt.docker.service.DockerServiceOperations
import com.logicovercode.fsbt.commons.SbtService
import com.logicovercode.wdocker.OsFunctions.currentOsOption
import com.logicovercode.wdocker.api.{DockerContext, DockerProcessFunctions}
import sbt.Keys._
import sbt.{Def, _}

trait PublicDockerSettings{
  lazy val ecrKeyForAws = settingKey[String]("ecr key for aws")
}

trait InternalDockerSettings extends PublicDockerSettings {

  lazy val dependentServices = settingKey[Set[SbtService]]("set of dependent docker services")
  lazy val startServices = taskKey[Unit]("start dependent containers")
  lazy val buildImage = inputKey[Unit]("build docker image on local with latest tag")
  lazy val buildImageForGitHub = inputKey[Unit]("build docker image on local with latest tag and github tag")

  lazy val buildImageForAws = inputKey[Unit]("build docker image on local with latest tag and aws tag. ecr key should be set to use this task")

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

      val buildImageMeta = prepareImageMeta(args, organization.value, name.value)

      val ghcrImageMeta = BuildImageMetaData(s"ghcr.io/${buildImageMeta.imageName}", buildImageMeta.executionDirectory,
        buildImageMeta.dockerFile, buildImageMeta.dockerArgs)

      buildDockerImage(ghcrImageMeta, "latest")
      tagDockerImage(ghcrImageMeta.imageName, "latest", version.value)
    },

    ecrKeyForAws := "",

    buildImageForAws := {
      import complete.DefaultParsers._
      val ecrKeyString = ecrKeyForAws.value
      Option(ecrKeyString).filter(_.trim.nonEmpty) match {
        case Some(ecrKey) =>
          val args = spaceDelimited("").parsed

          val buildImageMeta = prepareImageMeta(args, organization.value, name.value)

          val effectiveName = buildImageMeta.imageName.split("/").last

          val ecrImageMeta = BuildImageMetaData(s"$ecrKey/$effectiveName", buildImageMeta.executionDirectory,
            buildImageMeta.dockerFile, buildImageMeta.dockerArgs)

          buildDockerImage(ecrImageMeta, "latest")
          tagDockerImage(ecrImageMeta.imageName, "latest", version.value)

        case None => println("ecrKeyForAws should be defined to build image for aws ecr")
      }

    }
  )
}
