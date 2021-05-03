package org.logicovercode.bsbt

import org.logicovercode.bsbt.build.Build
import org.logicovercode.bsbt.docker.{DockerImplicitConversions, DockerSettings}
import org.logicovercode.bsbt.module_id.ModuleIDImplicitConversions
import org.logicovercode.bsbt.paths.PluginPathSettings
import org.logicovercode.bsbt.project.BuildProjectExtensions
import org.logicovercode.bsbt.publishing.BuildPublishingExtensions
import org.logicovercode.bsbt.sbt_module.SbtModuleSettings
import org.logicovercode.bsbt.scalafmt.ScalaFmtSettings
import sbt.AutoPlugin

/** Created by mogli on 4/23/2017.
  */
object BuilderStyleBuild extends AutoPlugin with DockerSettings with ScalaFmtSettings {

  object autoImport extends ModuleIDImplicitConversions
    with BuildProjectExtensions
    with BuildPublishingExtensions
    with SbtModuleSettings with PluginPathSettings with ScalaFmtSettings with DockerImplicitConversions

  override lazy val projectSettings = super.projectSettings ++ dockerSettings ++ scalaFmtSettings
}
