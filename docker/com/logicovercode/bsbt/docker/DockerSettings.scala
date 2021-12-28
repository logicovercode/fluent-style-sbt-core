package com.logicovercode.bsbt.docker

import com.logicovercode.bsbt.docker.model.{DockerInfra, MicroService}
import com.logicovercode.bsbt.docker.utils.{BuildImageMetaData, DockerServiceOperations}
import com.logicovercode.bsbt.docker.model.MicroService
import com.logicovercode.bsbt.docker.utils.DockerCliOperations._
import com.logicovercode.bsbt.docker.utils.BuildImageMetaData
import com.logicovercode.bsbt.os.OsFunctions.currentOsOption
import sbt.Keys._
import sbt.{Def, _}

import scala.util.{Failure, Success, Try}

trait DockerSettings {

  lazy val dependentServices = settingKey[Set[MicroService]]("set of dependent docker services")
  lazy val startServices = taskKey[Unit]("start dependent containers")
  lazy val buildImage = inputKey[Unit]("build docker image on local with latest tag")
  lazy val buildImageForGitHub = inputKey[Unit]("build docker image on local with latest tag and github tag")

  lazy val dockerSettings = Seq[Def.Setting[_]](
    dependentServices := Set(),

    startServices := {
      val dockerServices = (dependentServices.value)

      val allNetworks = (for {
        dockerService <- dockerServices
        containerDefinition <- dockerService.serviceDescriptions
        network = containerDefinition.container.networkMode
      } yield network).flatten

      implicit val dockerClient = DockerInfra.dockerClient

      val status = for{
        nets <- Try(allNetworks)
        createTries = nets.map(n => DockerSystem.createNonExistingNetwork(n))
        createTry <- Try( createTries.map(_.get) )
      } yield (createTry)

      status match {
        case Success(s) => println(s)
        case Failure(ex) => ex.printStackTrace()
      }

      dockerServices.par.foreach(DockerServiceOperations.startService)

      val sbtProcessId = DockerServiceOperations.pid()
      DockerServiceOperations.killDockerManager(sbtProcessId, currentOsOption)
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

  private def prepareImageMeta(args : Seq[String], org : String, name : String) : BuildImageMetaData = {
    val dockerParsingResult = parseDockerCommandArgs(args)

    val dockerImageName = imageName(org, name + dockerParsingResult.suffix)

    BuildImageMetaData(
      dockerImageName,
      dockerParsingResult.executionDirectory,
      dockerParsingResult.dockerFile,
      dockerParsingResult.dockerArgs
    )
  }
}
