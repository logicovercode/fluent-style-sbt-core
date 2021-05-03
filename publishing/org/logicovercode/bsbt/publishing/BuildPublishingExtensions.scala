package org.logicovercode.bsbt.publishing

import org.apache.ivy.core.module.descriptor.License
import org.logicovercode.bsbt.build.{Build, BuildFactory}
import org.logicovercode.bsbt.java_module.JavaBuild
import org.logicovercode.bsbt.scala_module.ScalaBuild
import sbt.Def
import sbt.librarymanagement.{Developer, MavenRepository, ScmInfo}

import java.net.URL

trait BuildPublishingSettings[T <: Build[T]] extends BuildFactory[T] with PublishingSettings {
  def argsRequiredForPublishing(
                                 projectDevelopers: List[Developer],
                                 license: License,
                                 homePageUrl: URL,
                                 moduleScmInfo: ScmInfo,
                                 mavenRepository: MavenRepository
                               ): T = {

    val _settings = argsRequiredForPublishingSettings(projectDevelopers, license, homePageUrl, moduleScmInfo, mavenRepository)
    moduleWithNewSettings( _settings)
  }
}

trait BuildPublishingExtensions {

  case class GithubRepo private (
      private val githubUser: String,
      private val githubRepoName: String
  ) {

    def homePageUrl() = new URL(
      s"https://github.com/$githubUser/$githubRepoName"
    )
    private def sourceCodeBrowserUrl =
      s"https://github.com/$githubUser/$githubRepoName"
    private def sourceCodeScmUrl = s"git@github.com:$githubUser/$githubRepoName"
    def scmInfo(devConnection: Option[String] = None): ScmInfo = {
      ScmInfo(new URL(sourceCodeBrowserUrl), sourceCodeScmUrl, devConnection)
    }
  }

  implicit class JavaBuildSettingsExtension(javaBuildSettings: JavaBuild)
    extends BuildPublishingSettings[JavaBuild] {
    override def moduleWithNewSettings(publishingSettings: Set[Def.Setting[_]]): JavaBuild = {
      JavaBuild(javaBuildSettings.sbtSettings ++ publishingSettings)
    }
  }

  implicit class ScalaBuildSettingsExtension(scalaBuildSettings: ScalaBuild)
    extends BuildPublishingSettings[ScalaBuild] {
    override def moduleWithNewSettings(publishingSettings: Set[Def.Setting[_]]): ScalaBuild = {
      ScalaBuild(scalaBuildSettings.sbtSettings ++ publishingSettings)
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
