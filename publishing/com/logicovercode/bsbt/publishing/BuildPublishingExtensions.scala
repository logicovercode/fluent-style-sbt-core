package com.logicovercode.bsbt.publishing

import com.logicovercode.bsbt.java_module.JavaBuild
import com.logicovercode.bsbt.scala_module.ScalaBuild
import org.apache.ivy.core.module.descriptor.License
import sbt.librarymanagement.{MavenRepo, MavenRepository}
import sbt.{MavenRepository, Opts, url}
import xerial.sbt.Sonatype._



trait BuildPublishingExtensions {

  val newSonatypeStaging = MavenRepo(
    "new-sonatype-staging",
    "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2"
  )

  def githubHosting(organizationOrIndividual: String, repositoryName: String, contactPersonName: String, contactPersonEmail: String): ProjectHosting =
    GitHubHosting(organizationOrIndividual, repositoryName, contactPersonName, contactPersonEmail)

  implicit class JavaBuildPublishingSettingsExtension(javaBuildSettings: JavaBuild)
    extends BuildPublishingSettings[JavaBuild] {

    override def publish(projectDevelopers: List[sbt.Developer], license: License,
                         projectHosting: ProjectHosting, mavenRepository: MavenRepository = newSonatypeStaging): JavaBuild = {
      JavaBuild(
        javaBuildSettings.sbtSettings
          ++
          publishToSonatypeSettings(projectDevelopers, license, url(projectHosting.homepage), projectHosting.scmInfo, mavenRepository)
      )
    }

    override def publishWithoutSource(projectDevelopers: List[sbt.Developer], license: License,
                                                projectHosting: ProjectHosting, mavenRepository: MavenRepository = newSonatypeStaging): JavaBuild = {
      JavaBuild(
        javaBuildSettings.sbtSettings
          ++
          publishToSonatypeWithoutSourceSettings(projectDevelopers, license, url(projectHosting.homepage),
            projectHosting.scmInfo, mavenRepository)
      )
    }

  }

  implicit class ScalaBuildPublishingSettingsExtension(scalaBuildSettings: ScalaBuild)
    extends BuildPublishingSettings[ScalaBuild] {



    override def publish(projectDevelopers: List[sbt.Developer], license: License,
                         projectHosting: ProjectHosting, mavenRepository: MavenRepository = newSonatypeStaging): ScalaBuild = {
      ScalaBuild(
        scalaBuildSettings.sbtSettings
          ++
          publishToSonatypeSettings(projectDevelopers, license, url(projectHosting.homepage), projectHosting.scmInfo, mavenRepository)
      )
    }

    override def publishWithoutSource(projectDevelopers: List[sbt.Developer], license: License,
                                      projectHosting: ProjectHosting, mavenRepository: MavenRepository = newSonatypeStaging): ScalaBuild = {
      ScalaBuild(
        scalaBuildSettings.sbtSettings
          ++
          publishToSonatypeWithoutSourceSettings(projectDevelopers, license, url(projectHosting.homepage),
            projectHosting.scmInfo, mavenRepository)
      )
    }
  }
}
