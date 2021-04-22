package org.logicovercode.bsbt.docker

import org.scalatest.flatspec.AsyncFlatSpecLike
import org.scalatest.matchers.should.Matchers

class DockerCommandParserSpec extends AsyncFlatSpecLike with Matchers {

  val dataset1 = {
    val input = List[String]()
    val output = (".", "Dockerfile")
    (input, output)
  }

  val dataset2 = {
    val input = List[String]("dir=.")
    val output = (".", "Dockerfile")
    (input, output)
  }

  val dataset3 = {
    val input = List[String]("dir=.", "file=Dockerfile")
    val output = (".", "Dockerfile")
    (input, output)
  }

  val dataset4 = {
    val input = List[String]("dir=.", "dfile=Dofile")
    val output = (".", "Dockerfile")
    (input, output)
  }

  val dataset5 = {
    val input = List[String]("dir=docker", "dfile=Dofile")
    val output = ("docker", "Dockerfile")
    (input, output)
  }

  val dataset6 = {
    val input = List[String]("dir=docker", "file=Dofile")
    val output = ("docker", "Dofile")
    (input, output)
  }

  val data = Set(dataset1, dataset2, dataset3, dataset4, dataset5, dataset6)

  private val dockerCommandOperations = new DockerCommandOperations {}

  import dockerCommandOperations._

  data foreach { case (input, output) =>
    it should s"return $output for $input" in {
      parseDockerCommandArgs(input) should be(output)
    }
  }
}
