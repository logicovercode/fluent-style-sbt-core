package com.logicovercode.bsbt.docker.cli

case class DockerParsingResult(executionDirectory : String, dockerFile : String, suffix : String, dockerArgs : String)

case class BuildImageMetaData(imageName : String, executionDirectory : String, dockerFile : String, dockerArgs : String){
  def buildImageCommand(tag : String) : String = {
    s"docker build -t ${imageName}:$tag ${dockerArgs} -f $dockerFile $executionDirectory"
  }
}
