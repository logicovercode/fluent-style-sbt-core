package org.logicovercode.bsbt

import org.logicovercode.bsbt.docker.{DockerImplicitConversions, DockerSettings}
import org.logicovercode.bsbt.module_id.ModuleIDImplicitConversions
import org.logicovercode.bsbt.paths.PluginPathSettings
import org.logicovercode.bsbt.project.BuildSettingsProjectExtensions
import org.logicovercode.bsbt.publishing.BuildSettingsPublishingExtensions
import org.logicovercode.bsbt.scalafmt.ScalaFmtSettings
import sbt.AutoPlugin

/** Created by mogli on 4/23/2017.
  */
object BuilderStyleBuild extends AutoPlugin with DockerSettings with ScalaFmtSettings {

  object autoImport extends ModuleIDImplicitConversions
    with BuildSettingsProjectExtensions
    with BuildSettingsPublishingExtensions
    with PluginPathSettings with ScalaFmtSettings with DockerImplicitConversions

  override lazy val projectSettings = super.projectSettings ++ dockerSettings ++ scalaFmtSettings
}
