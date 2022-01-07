package com.logicovercode.bsbt.publishing

import com.logicovercode.bsbt.java_module.JavaBuild
import com.logicovercode.bsbt.scala_module.ScalaBuild
import org.apache.ivy.core.module.descriptor.License
import sbt.{Def, url}
import sbt.librarymanagement.ScmInfo
import xerial.sbt.Sonatype._


import java.net.URL



trait BuildPublishingExtensions {

  def githubHosting(organizationOrIndividual: String, repositoryName: String, contactPersonName: String, contactPersonEmail: String): ProjectHosting =
    GitHubHosting(organizationOrIndividual, repositoryName, contactPersonName, contactPersonEmail)

  implicit class JavaBuildPublishingSettingsExtension(javaBuildSettings: JavaBuild)
    extends BuildPublishingSettings[JavaBuild] {

    override def publishTo(projectDevelopers: List[sbt.Developer],
                                           license: License, homePageUrl: URL,
                                           moduleScmInfo: sbt.ScmInfo,
                                           mavenRepository: sbt.MavenRepository): JavaBuild = {
      JavaBuild(
        javaBuildSettings.sbtSettings
        ++
          publishToSettings(projectDevelopers, license, homePageUrl, moduleScmInfo, mavenRepository)
      )
    }

    override def publishToSonatype(projectDevelopers: List[sbt.Developer], license: License, projectHosting: ProjectHosting): JavaBuild = {
      JavaBuild(
        javaBuildSettings.sbtSettings
          ++
          publishToSonatypeSettings(projectDevelopers, license, url(projectHosting.homepage), projectHosting.scmInfo)
      )
    }

    override def publishToSonatypeWithoutSource(projectDevelopers: List[sbt.Developer], license: License, projectHosting: ProjectHosting): JavaBuild = {
      JavaBuild(
        javaBuildSettings.sbtSettings
          ++
          publishToSonatypeWithoutSourceSettings(projectDevelopers, license, url(projectHosting.homepage), projectHosting.scmInfo)
      )
    }
  }

  implicit class ScalaBuildPublishingSettingsExtension(scalaBuildSettings: ScalaBuild)
    extends BuildPublishingSettings[ScalaBuild] {

    override def publishTo(projectDevelopers: List[sbt.Developer],
                                           license: License, homePageUrl: URL,
                                           moduleScmInfo: sbt.ScmInfo,
                                           mavenRepository: sbt.MavenRepository): ScalaBuild = {
      ScalaBuild(
        scalaBuildSettings.sbtSettings
          ++
          publishToSettings(projectDevelopers, license, homePageUrl, moduleScmInfo, mavenRepository)
      )
    }

    override def publishToSonatype(projectDevelopers: List[sbt.Developer], license: License, projectHosting: ProjectHosting): ScalaBuild = {
      ScalaBuild(
        scalaBuildSettings.sbtSettings
          ++
          publishToSonatypeSettings(projectDevelopers, license, url(projectHosting.homepage), projectHosting.scmInfo)
      )
    }

    override def publishToSonatypeWithoutSource(projectDevelopers: List[sbt.Developer], license: License, projectHosting: ProjectHosting): ScalaBuild = {
      ScalaBuild(
        scalaBuildSettings.sbtSettings
          ++
          publishToSonatypeWithoutSourceSettings(projectDevelopers, license, url(projectHosting.homepage), projectHosting.scmInfo)
      )
    }
  }


    //  object GithubRepo{
//    def apply(githubUser : String, githubRepoName : String) : GithubRepo = new GithubRepo(githubUser, githubRepoName)
//  }

  //  final class GithubRepo private(private val githubUser : String, private val githubRepoName : String)
//    extends Serializable{
//
//    def homePageUrl = new URL(s"https://github.com/$githubUser/$githubRepoName")
//    private def sourceCodeBrowserUrl = s"https://github.com/$githubUser/$githubRepoName"
//    private def sourceCodeScmUrl = s"git@github.com:$githubUser/$githubRepoName"
//    def scmInfo(devConnection: Option[String] = None) : ScmInfo = {
//      ScmInfo(new URL(sourceCodeBrowserUrl), sourceCodeScmUrl, devConnection)
//    }
//
//    override def equals(obj: Any): Boolean = Objects.equals(this, obj)
//    override def hashCode(): Int = Objects.hashCode(this)
//
//    override def toString: String = s"GithubRepo[user >$githubUser<, repo >$githubRepoName<]"
//  }
//
//  object GithubRepo{
//    def apply(githubUser : String, githubRepoName : String) : GithubRepo = new GithubRepo(githubUser, githubRepoName)
//  }
}
