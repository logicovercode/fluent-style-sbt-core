package com.logicovercode.bsbt.publishing

import com.logicovercode.bsbt.java_module.JavaBuild
import com.logicovercode.bsbt.scala_module.ScalaBuild
import org.apache.ivy.core.module.descriptor.License
import sbt.Def
import sbt.librarymanagement.ScmInfo

import java.net.URL



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

  implicit class JavaBuildPublishingSettingsExtension(javaBuildSettings: JavaBuild)
    extends BuildPublishingSettings[JavaBuild] {

    override def argsRequiredForPublishing(projectDevelopers: List[sbt.Developer],
                                           license: License, homePageUrl: URL,
                                           moduleScmInfo: sbt.ScmInfo,
                                           mavenRepository: sbt.MavenRepository): JavaBuild = {
      JavaBuild(
        javaBuildSettings.sbtSettings
        ++
          argsRequiredForPublishingSettings(projectDevelopers, license, homePageUrl, moduleScmInfo, mavenRepository)
      )
    }
  }

  implicit class ScalaBuildPublishingSettingsExtension(scalaBuildSettings: ScalaBuild)
    extends BuildPublishingSettings[ScalaBuild] {

    override def argsRequiredForPublishing(projectDevelopers: List[sbt.Developer],
                                           license: License, homePageUrl: URL,
                                           moduleScmInfo: sbt.ScmInfo,
                                           mavenRepository: sbt.MavenRepository): ScalaBuild = {
      ScalaBuild(
        scalaBuildSettings.sbtSettings
          ++
          argsRequiredForPublishingSettings(projectDevelopers, license, homePageUrl, moduleScmInfo, mavenRepository)
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
