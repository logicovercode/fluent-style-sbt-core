package org.logicovercode.bsbt.docker.utils

case class DockerParsingResult(executionDirectory : String, dockerFile : String, suffix : String, dockerArgs : String)

case class BuildImageMetaData(imageName : String, executionDirectory : String, dockerFile : String, dockerArgs : String){
  def buildImageCommand : String = {
    s"docker build -t ${imageName} ${dockerArgs} -f $dockerFile $executionDirectory"
  }
}
