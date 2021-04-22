package org.logicovercode.bsbt.scalafmt
import better.files.File
import sbt._

trait ScalaFmtSettings {

  lazy val generateBasicScalaFmtConfig = taskKey[Boolean]("generates a basic .scalfmt file in root directory, if doesn't exists")
  lazy val showBasicScalaFmtConfig = taskKey[Unit]("show basic .scalfmt config")

  lazy val scalaFmtSettings = Seq[Def.Setting[_]](
    generateBasicScalaFmtConfig := {
      val file = File(".scalafmt.conf")
      file.exists match {
        case true =>
          println("not generating .scalafmt, as it already exists")
          false

        case false => {
          println("now generating .scalafmt, as it don't exists")
          import better.files.Dsl._
          touch(file)
          file.append(scalaFmtConfig)
          true
        }
      }
    },
    showBasicScalaFmtConfig := {
      println
      println("you can add below configuration to .scalafmt config :-")
      println("-" * 30)
      println(scalaFmtConfig)
      println("-" * 30)
    }
  )

  private def scalaFmtConfig: String = {
    """
      |version = 2.7.5
      |maxColumn = 140
      |""".stripMargin
  }
}
