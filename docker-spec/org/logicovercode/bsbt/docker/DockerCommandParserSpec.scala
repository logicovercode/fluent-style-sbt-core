package org.logicovercode.bsbt.docker

import org.logicovercode.bsbt.docker.utils.{DockerCliOperations, DockerParsingResult}
import org.scalatest.flatspec.AsyncFlatSpecLike
import org.scalatest.matchers.should.Matchers

class DockerCommandParserSpec extends AsyncFlatSpecLike with Matchers {

  val dataset1 = {
    val input = List[String]()
    val output = DockerParsingResult(".", "Dockerfile", "", "")
    (input, output)
  }

  val dataset2 = {
    val input = List[String]("dir=.")
    val output = DockerParsingResult(".", "Dockerfile", "", "")
    (input, output)
  }

  val dataset3 = {
    val input = List[String]("dir=.", "file=Dockerfile")
    val output = DockerParsingResult(".", "Dockerfile", "", "")
    (input, output)
  }

  val dataset4 = {
    val input = List[String]("dir=.", "dfile=Dofile")
    val output = DockerParsingResult(".", "Dockerfile", "", "--build-arg dfile=Dofile")
    (input, output)
  }

  val dataset5 = {
    val input = List[String]("dir=docker", "dfile=Dofile")
    val output = DockerParsingResult("docker", "Dockerfile", "", "--build-arg dfile=Dofile")
    (input, output)
  }

  val dataset6 = {
    val input = List[String]("dir=docker", "file=Dofile")
    val output = DockerParsingResult("docker", "Dofile", "", "")
    (input, output)
  }

  val dataset7 = {
    val input = List[String]("dir=.", "file=Dockerfile-dev")
    val output = DockerParsingResult(".", "Dockerfile-dev", "-dev", "")
    (input, output)
  }

  val dataset8 = {
    val input = List[String]("dir=.", "file=DFile-dev")
    val output = DockerParsingResult(".", "DFile-dev", "-dev", "")
    (input, output)
  }

  val data = Set(dataset1, dataset2, dataset3, dataset4, dataset5, dataset6, dataset7, dataset8)
  //val data = Set(dataset4)

  data foreach { case (input, output) =>
    it should s"return $output for $input" in {
      val expectedDockerParsingResult = DockerCliOperations.parseDockerCommandArgs(input)
      expectedDockerParsingResult should be(output)
    }
  }
}
