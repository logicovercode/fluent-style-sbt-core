package org.logicovercode.bsbt.publishing

import org.apache.ivy.core.module.descriptor.License
import org.logicovercode.bsbt.core_module.BuildSettings
import org.logicovercode.bsbt.java_module.JavaBuildSettings
import org.logicovercode.bsbt.scala_module.ScalaBuildSettings
import sbt.librarymanagement.{Developer, MavenRepository, ScmInfo}

import java.net.URL

trait BuildPublishingSettings[T <: BuildSettings[T]]{
  def argsRequiredForPublishing(
                                 projectDevelopers: List[Developer],
                                 license: License,
                                 homePageUrl: URL,
                                 moduleScmInfo: ScmInfo,
                                 mavenRepository: MavenRepository
                               ): T
}

trait BuildSettingsPublishingExtensions {

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

  protected implicit class JavaBuildSettingsExtension(javaBuildSettings: JavaBuildSettings)
    extends BuildPublishingSettings[JavaBuildSettings]
      with PublishingSettings {
    override def argsRequiredForPublishing(projectDevelopers: List[Developer],
                                           license: License, homePageUrl: URL,
                                           moduleScmInfo: ScmInfo,
                                           mavenRepository: MavenRepository): JavaBuildSettings = {
      val _settings = argsRequiredForPublishingSettings(projectDevelopers, license, homePageUrl, moduleScmInfo, mavenRepository)
      JavaBuildSettings( javaBuildSettings.sbtSettings ++ _settings)
    }
  }

  protected implicit class ScalaBuildSettingsExtension(scalaBuildSettings: ScalaBuildSettings)
    extends BuildPublishingSettings[ScalaBuildSettings]
      with PublishingSettings {
    override def argsRequiredForPublishing(projectDevelopers: List[Developer],
                                           license: License, homePageUrl: URL,
                                           moduleScmInfo: ScmInfo,
                                           mavenRepository: MavenRepository): ScalaBuildSettings = {
      val _settings = argsRequiredForPublishingSettings(projectDevelopers, license, homePageUrl, moduleScmInfo, mavenRepository)
      ScalaBuildSettings( scalaBuildSettings.sbtSettings ++ _settings)
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
