package com.logicovercode.bsbt

import com.logicovercode.bsbt.docker.DockerSettings
import com.logicovercode.bsbt.fsbt_attributes.FSbtAttributesImplicitConversions
import com.logicovercode.bsbt.module_id.ModuleIDImplicitConversions
import com.logicovercode.bsbt.project.BuildProjectExtensions
import com.logicovercode.bsbt.publishing.BuildPublishingExtensions
import com.logicovercode.bsbt.scalafmt.ScalaFmtSettings
import sbt.AutoPlugin

/** Created by mogli on 4/23/2017.
  */
object BuilderStyleBuild extends AutoPlugin with DockerSettings with ScalaFmtSettings {

  object autoImport extends ModuleIDImplicitConversions
    with BuildProjectExtensions
    with BuildPublishingExtensions
    with FSbtAttributesImplicitConversions
    with ScalaFmtSettings with FSbtCoreTypeAliases

  override lazy val projectSettings = super.projectSettings ++ dockerSettings ++ scalaFmtSettings
}
