package com.logicovercode.bsbt.fsbt_attributes

import org.apache.ivy.core.module.descriptor.License
import sbt._
import xerial.sbt.Sonatype._

trait FSbtAttributesImplicitConversions {

  implicit def LicenseToTuple(license : License): (String, URL) = {
    (license.getName, new URL(license.getUrl))
  }

  implicit def developerToDevelopers(developer: Developer): List[Developer] = List(developer)

  implicit def gitHubHostingToMayBeGitHubHosting(projectHosting: ProjectHosting) : Option[ProjectHosting] = {
    Option(GitHubHosting("username", "projectName", "user@example.com"))
  }
}
