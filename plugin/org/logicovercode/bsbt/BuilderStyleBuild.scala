package org.logicovercode.bsbt

import org.logicovercode.bsbt.core.{ModuleIDSettings, ProjectSettings}
import org.logicovercode.bsbt.docker.{DockerExtension, DockerSettings}
import org.logicovercode.bsbt.paths.PluginPaths
import org.logicovercode.bsbt.publishing.PublishingSettings
import org.logicovercode.bsbt.core.ModuleIDSettings
import org.logicovercode.bsbt.scalafmt.ScalaFmtSettings
import sbt.AutoPlugin

/** Created by mogli on 4/23/2017.
  */
object BuilderStyleBuild extends AutoPlugin with DockerSettings with ScalaFmtSettings {

  object autoImport extends PublishingSettings with ModuleIDSettings
    with ProjectSettings with PluginPaths with ScalaFmtSettings with DockerExtension

  override lazy val projectSettings = super.projectSettings ++ dockerSettings ++ scalaFmtSettings
}
