package com.logicovercode.bsbt.publishing

import org.apache.ivy.core.module.descriptor.License
import sbt.{Compile, Def, Opts}
import sbt.Keys._
import sbt.librarymanagement.{Developer, MavenRepository, ScmInfo}

import java.net.URL

trait PublishingSettings {

  final def publishToSettings(projectDevelopers: List[Developer],
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
      publishMavenStyle := true,
      publishTo := Some(mavenRepository)
    )

    _settings.toSet
  }

  final def publishToSonatypeSettings(projectDevelopers: List[Developer],
                                               license: License,
                                               homePageUrl: URL,
                                               moduleScmInfo: ScmInfo
                                             ): Set[Def.Setting[_]] = {

    val _settings = Set(
      licenses += (license.getName, new URL(license.getUrl)),
      homepage := Option(homePageUrl),
      scmInfo := Option(moduleScmInfo),
      developers := projectDevelopers,
      publishTo := Some(Opts.resolver.sonatypeStaging)
    )

    _settings.toSet
  }

  final def publishToSonatypeWithoutSourceSettings(projectDevelopers: List[Developer],
                               license: License,
                               homePageUrl: URL,
                               moduleScmInfo: ScmInfo
                             ): Set[Def.Setting[_]] = {

    val _settings = Set(
      licenses += (license.getName, new URL(license.getUrl)),
      homepage := Option(homePageUrl),
      scmInfo := Option(moduleScmInfo),
      developers := projectDevelopers,
      publishTo := Some(Opts.resolver.sonatypeStaging),
      Compile / packageSrc / publishArtifact := false
    )

    _settings.toSet
  }
}
