package com.logicovercode.bsbt.publishing

import com.logicovercode.bsbt.build.Build
import org.apache.ivy.core.module.descriptor.License
import sbt._
import xerial.sbt.Sonatype.ProjectHosting

import java.net.URL

trait BuildPublishingSettings[T <: Build[T]] extends PublishingSettings {
  @Deprecated
  def publishTo(
                                 projectDevelopers: List[Developer],
                                 license: License,
                                 homePageUrl: URL,
                                 moduleScmInfo: ScmInfo,
                                 mavenRepository: MavenRepository
                               ): T

  def publishToSonatype(
                                 projectDevelopers: List[Developer],
                                 license: License,
                                 projectHosting: ProjectHosting
                               ): T

  def publishToSonatypeWithoutSource(
                         projectDevelopers: List[Developer],
                         license: License,
                         projectHosting: ProjectHosting
                       ): T
}

