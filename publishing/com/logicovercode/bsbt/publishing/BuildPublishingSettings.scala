package com.logicovercode.bsbt.publishing

import com.logicovercode.bsbt.build.Build
import org.apache.ivy.core.module.descriptor.License
import sbt._

import java.net.URL

trait BuildPublishingSettings[T <: Build[T]] extends PublishingSettings {
  def argsRequiredForPublishing(
                                 projectDevelopers: List[Developer],
                                 license: License,
                                 homePageUrl: URL,
                                 moduleScmInfo: ScmInfo,
                                 mavenRepository: MavenRepository
                               ): T
}
