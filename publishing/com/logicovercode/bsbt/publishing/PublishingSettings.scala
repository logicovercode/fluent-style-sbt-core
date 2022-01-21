package com.logicovercode.bsbt.publishing

import org.apache.ivy.core.module.descriptor.License
import sbt.Keys._
import sbt.librarymanagement.{Developer, MavenRepository, ScmInfo}
import sbt.{Compile, Def}
import xerial.sbt.Sonatype.autoImport.{sonatypeCredentialHost, sonatypePublishToBundle, sonatypeRepository}

import java.net.URL

trait PublishingSettings {

  final def publishToSonatypeSettings(
      projectDevelopers: List[Developer],
      license: License,
      homePageUrl: URL,
      moduleScmInfo: ScmInfo,
      mavenRepository: MavenRepository
  ): Set[Def.Setting[_]] = {


    scmSettings(projectDevelopers, license, homePageUrl, moduleScmInfo) ++ sonatypeHostSettings(mavenRepository)
  }

  final def publishToSonatypeWithoutSourceSettings(
      projectDevelopers: List[Developer],
      license: License,
      homePageUrl: URL,
      moduleScmInfo: ScmInfo,
      mavenRepository: MavenRepository
  ): Set[Def.Setting[_]] = {

    val disablePublishingSourceSettings = Set(
      Compile / packageSrc / publishArtifact := false
    )

    disablePublishingSourceSettings ++
      scmSettings(projectDevelopers, license, homePageUrl, moduleScmInfo) ++
      sonatypeHostSettings(mavenRepository)
  }

  private def sonatypeHostSettings(mavenRepository: MavenRepository): Set[Def.Setting[_]] = {
    val sonatypeHostSettings: Set[Def.Setting[_]] = if (mavenRepository.name.equals("new-sonatype-staging")) {
      val credentialsHost = "s01.oss.sonatype.org"
      println(s"using sonatypeCredentialHost host : >$credentialsHost<")
      Set(
        sonatypeCredentialHost := credentialsHost,
        publishTo := Option(mavenRepository)
      )
    } else {
      println("using default configured host : >oss.sonatype.org<")
      Set(
        publishTo := Option(mavenRepository)
      )
    }
    sonatypeHostSettings
  }

  private def scmSettings(projectDevelopers: List[Developer], license: License,
                          homePageUrl: URL,
                          moduleScmInfo: ScmInfo) : Set[Def.Setting[_]]  = {
    val _settings : Set[Def.Setting[_]] = Set(
      licenses += (license.getName, new URL(license.getUrl)),
      homepage := Option(homePageUrl),
      scmInfo := Option(moduleScmInfo),
      developers := projectDevelopers,
      //IMPORTANT, not required for sbt plugins as well as non-plugin sbt artifacts
      //publishMavenStyle := true,
    )
    _settings
  }
}
