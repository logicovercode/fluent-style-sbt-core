package org.logicovercode.bsbt.publishing

import org.apache.ivy.core.module.descriptor.License
import org.logicovercode.bsbt.core_module.model.BuildSettings
import sbt.Keys.{publishTo, _}
import sbt.librarymanagement.{Developer, MavenRepository, ScmInfo}

import java.net.URL
import java.util.Objects

trait PublishingSettings extends PublishingModel {

  implicit class ModuleBuildArtifactPublishExtension(moduleBuild: BuildSettings[_]) {

    def argsRequiredForPublishing(
        projectDevelopers: List[Developer],
        license: License,
        homePageUrl: URL,
        moduleScmInfo: ScmInfo,
        mavenRepository: MavenRepository
    ): BuildSettings[_] = {

      println("adding publishing settings")
      val _settings = Set(
        licenses += (license.getName, new URL(license.getUrl)),
        homepage := Option(homePageUrl),
        scmInfo := Option(moduleScmInfo),
        developers := projectDevelopers,
        publishMavenStyle := true,
        publishTo := Some(mavenRepository)
      )

      val allSettings = moduleBuild.settings.toSet ++ _settings
      //ModuleBuild(allSettings)
      null
    }
  }

}
