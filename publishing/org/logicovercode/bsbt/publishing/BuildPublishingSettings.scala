package org.logicovercode.bsbt.publishing

import org.apache.ivy.core.module.descriptor.License
import org.logicovercode.bsbt.build.Build
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

