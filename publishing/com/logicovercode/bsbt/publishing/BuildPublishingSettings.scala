package com.logicovercode.bsbt.publishing

import com.logicovercode.bsbt.build.Build
import org.apache.ivy.core.module.descriptor.License
import sbt._
import xerial.sbt.Sonatype.ProjectHosting

trait BuildPublishingSettings[T <: Build[T]] extends PublishingSettings {
  def publish(projectDevelopers: List[Developer],
              license: License,
              projectHosting: ProjectHosting, mavenRepository : MavenRepository): T

  def publishWithoutSource(projectDevelopers: List[Developer], license: License,
                           projectHosting: ProjectHosting, mavenRepository : MavenRepository): T
}
