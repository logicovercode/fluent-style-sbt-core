package com.logicovercode.bsbt

import com.logicovercode.bsbt.docker.{DockerImplicitConversions, DockerSettings}
import com.logicovercode.bsbt.module_id.ModuleIDImplicitConversions
import com.logicovercode.bsbt.paths.PluginPathSettings
import com.logicovercode.bsbt.project.BuildProjectExtensions
import com.logicovercode.bsbt.publishing.BuildPublishingExtensions
import com.logicovercode.bsbt.sbt_module.SbtModuleSettings
import com.logicovercode.bsbt.scalafmt.ScalaFmtSettings
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
