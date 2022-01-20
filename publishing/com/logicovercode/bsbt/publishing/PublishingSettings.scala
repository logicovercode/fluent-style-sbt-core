package com.logicovercode.bsbt.publishing

import org.apache.ivy.core.module.descriptor.License
import sbt.Keys._
import sbt.librarymanagement.{Developer, MavenRepository, ScmInfo}
import sbt.{Compile, Def}
import xerial.sbt.Sonatype.autoImport.{sonatypeCredentialHost, sonatypeRepository}

import java.net.URL

trait PublishingSettings {

  final def publishToSettings(
      projectDevelopers: List[Developer],
      license: License,
      homePageUrl: URL,
      moduleScmInfo: ScmInfo,
      mavenRepository: MavenRepository
  ): Set[Def.Setting[_]] = {

    val _settings = Set(
      licenses += (license.getName, new URL(license.getUrl)),
      homepage := Option(homePageUrl),
      scmInfo := Option(moduleScmInfo),
      developers := projectDevelopers,
      //IMPORTANT, not required for sbt plugins as well as non-plugin sbt artifacts
      //publishMavenStyle := true,
      publishTo := Some(mavenRepository)
    )

    _settings.toSet
  }

  final def publishToSonatypeSettings(
      projectDevelopers: List[Developer],
      license: License,
      homePageUrl: URL,
      moduleScmInfo: ScmInfo,
      useNewSonatypeHost : Boolean,
      mavenRepository: MavenRepository
  ): Set[Def.Setting[_]] = {

    val _settings = Set(
      licenses += (license.getName, new URL(license.getUrl)),
      homepage := Option(homePageUrl),
      scmInfo := Option(moduleScmInfo),
      developers := projectDevelopers,
      //IMPORTANT, not required for sbt plugins as well as non-plugin sbt artifacts
      //publishMavenStyle := true,
      publishTo := Some(mavenRepository)
    )

    val sonatypeHostSettings : Set[Def.Setting[_]] = if(useNewSonatypeHost){
      val credentialsHost = "s01.oss.sonatype.org"
      val repositoryHost = "https://s01.oss.sonatype.org/service/local"
      println(s"using sonatypeCredentialHost host : >$credentialsHost<")
      println(s"using sonatypeRepository host : >$repositoryHost<")
      Set(
        sonatypeCredentialHost := credentialsHost,
        sonatypeRepository := repositoryHost
      )
    }else{
      println("using default configured host : >oss.sonatype.org<")
      Set()
    }

    _settings ++ sonatypeHostSettings
  }

  final def publishToSonatypeWithoutSourceSettings(
      projectDevelopers: List[Developer],
      license: License,
      homePageUrl: URL,
      moduleScmInfo: ScmInfo,
      useNewSonatypeHost : Boolean,
      mavenRepository: MavenRepository
  ): Set[Def.Setting[_]] = {

    val _settings = Set(
      licenses += (license.getName, new URL(license.getUrl)),
      homepage := Option(homePageUrl),
      scmInfo := Option(moduleScmInfo),
      developers := projectDevelopers,
      //IMPORTANT, not required for sbt plugins as well as non-plugin sbt artifacts
      //publishMavenStyle := true,
      publishTo := Some(mavenRepository),
      Compile / packageSrc / publishArtifact := false
    )

    val sonatypeHostSettings : Set[Def.Setting[_]] = if(useNewSonatypeHost){
      val host = "s01.oss.sonatype.org"
      println(s"using new sonatype host : >$host<")
      Set(sonatypeCredentialHost := host)
    }else{
      println("using default configured host : >oss.sonatype.org<")
      Set()
    }

    _settings ++ sonatypeHostSettings
  }
}
