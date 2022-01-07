package com.logicovercode.bsbt.publishing

import org.apache.ivy.core.module.descriptor.License
import sbt.Keys._
import sbt.librarymanagement.{Developer, MavenRepository, ScmInfo}
import sbt.{Compile, Def}

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

  final def publishToSonatypeWithoutSourceSettings(
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
      publishTo := Some(mavenRepository),
      Compile / packageSrc / publishArtifact := false
    )

    _settings.toSet
  }
}
