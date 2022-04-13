package com.logicovercode.bsbt

import com.logicovercode.bsbt.docker.InternalDockerSettings
import com.logicovercode.bsbt.fsbt_attributes.FSbtAttributesImplicitConversions
import com.logicovercode.bsbt.module_id.ModuleIDImplicitConversions
import com.logicovercode.bsbt.project.BuildProjectExtensions
import com.logicovercode.bsbt.publishing.BuildPublishingExtensions
import com.logicovercode.bsbt.scalafmt.ScalaFmtSettings
import sbt.AutoPlugin

/** Created by mogli on 4/23/2017.
  */
object BuilderStyleBuild extends AutoPlugin with InternalDockerSettings with ScalaFmtSettings {

  object autoImport extends ModuleIDImplicitConversions
    with BuildProjectExtensions
    with BuildPublishingExtensions
    with FSbtAttributesImplicitConversions
    with ScalaFmtSettings with FSbtCoreTypeAliases with ScalaVersions

  override lazy val projectSettings = super.projectSettings ++ dockerSettings ++ scalaFmtSettings
}
