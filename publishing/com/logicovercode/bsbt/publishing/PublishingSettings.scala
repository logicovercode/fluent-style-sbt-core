package com.logicovercode.bsbt.publishing

import org.apache.ivy.core.module.descriptor.License
import sbt.Def
import sbt.Keys.{developers, homepage, licenses, publishMavenStyle, publishTo, scmInfo}
import sbt.librarymanagement.{Developer, MavenRepository, ScmInfo}

import java.net.URL

trait PublishingSettings {

  final def argsRequiredForPublishingSettings(
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
      publishMavenStyle := true,
      publishTo := Some(mavenRepository)
    )

    _settings.toSet
  }
}
