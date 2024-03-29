package com.logicovercode.bsbt.docker.cli

object DockerCliOperations {

  def buildDockerImage(buildImageMetaData: BuildImageMetaData, tag: String): Unit = {

    import sys.process._

    println(s"now building docker image with latest tag >$tag<")
    val buildCommand = buildImageMetaData.buildImageCommand(tag)
    println(buildCommand)
    buildCommand !
  }

  def tagDockerImage(imageName: String, sourceTag: String, destinationTag: String): Unit = {
    import sys.process._

    val sourceImage = s"$imageName:$sourceTag"
    val destinationImage = s"$imageName:$destinationTag"
    println(s"now tagging docker image $sourceImage to $destinationImage :-")
    val tagCommand = s"docker tag ${sourceImage} ${destinationImage}"
    println(tagCommand)
    tagCommand !
  }

  def prepareImageMeta(args: Seq[String], org: String, artifact: String): BuildImageMetaData = {
    val dockerParsingResult = parseDockerCommandArgs(args)

    val dockerImageName = imageName(org, artifact + dockerParsingResult.suffix)

    BuildImageMetaData(
      dockerImageName,
      dockerParsingResult.executionDirectory,
      dockerParsingResult.dockerFile,
      dockerParsingResult.dockerArgs
    )
  }

  private def imageName(organization: String, name: String): String = {
    val arr = organization.split("[.]")
    println(arr.mkString("::"))
    val org = if (arr.length > 1) arr(1) else organization
    val image = s"$org/$name"
    image
  }

  private def parseDockerCommandArgs(args: Seq[String]): DockerParsingResult = {

    println("all args")
    args.foreach(println)

    val argsParamString = (for {
      argParam <- args.filter(arg => !arg.startsWith("dir") && !arg.startsWith("file"))
      argParamValue = argParam.replace("args=", "")
      argParamWithoutQuote = argParamValue.replace("\"", "")
    } yield argParamWithoutQuote).mkString(" ")

    println(s"arg param string >$argsParamString<")

    val tuples = args.map { arg =>
      val params = arg.split("=")
      params match {
        case Array("dir", dirValue)   => ("dir" -> dirValue)
        case Array("file", fileValue) => ("file" -> fileValue)
        case _                        => ("", "")
      }
    }

    val map = (tuples :+ ("args" -> argsParamString)).filter(!_._1.trim.isEmpty).toMap

    val dockerArgs = map.get("args").getOrElse("")
    val dockerArgsWithBuildArg = dockerArgs.isEmpty match {
      case true  => ""
      case false => dockerArgs.split(" ").map("--build-arg " + _).map(_.trim).mkString(" ")
    }

    val dirArgs = map.get("dir").getOrElse(".")
    val fileArgs = map.get("file").getOrElse("Dockerfile")

    println(s"value for dir >$dirArgs<")
    println(s"value for file >$fileArgs<")
    println(s"value for args >$dockerArgs<, with buildArg >$dockerArgsWithBuildArg<")

    val firstIndexOfHifen = fileArgs.indexOf("-")
    val suffix = if (firstIndexOfHifen != -1) fileArgs.substring(firstIndexOfHifen + 1) else ""
    println(s"value for suffix >$suffix<")
    println(s"value for dockerArgsWithBuildArg >$dockerArgsWithBuildArg<")
    DockerParsingResult(dirArgs, fileArgs, suffix, dockerArgsWithBuildArg)
  }
}
