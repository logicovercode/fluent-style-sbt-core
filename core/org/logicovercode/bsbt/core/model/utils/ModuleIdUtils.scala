package org.logicovercode.bsbt.core.model.utils

import sbt.librarymanagement.Configuration
import sbt.librarymanagement.Configurations._

trait ModuleIdUtils {

  def configuration(str: String): Option[Configuration] = {
    val lowerCase = str.trim.toLowerCase
    val camelCase = lowerCase.capitalize
    camelCase match {
      case "Default"         => Option(Default)
      case "Compile"         => Option(Compile)
      case "IntegrationTest" => Option(IntegrationTest)
      case "Provided"        => Option(Provided)
      case "Runtime"         => Option(Runtime)
      case "Test"            => Option(Test)
      case "System"          => Option(System)
      case "Optional"        => Option(Optional)
      case "Pom"             => Option(Pom)
      //This is important, None is not returned
      case _ => throw new RuntimeException(camelCase + " not supported")
    }
  }
}

object ModuleIdUtils extends ModuleIdUtils
