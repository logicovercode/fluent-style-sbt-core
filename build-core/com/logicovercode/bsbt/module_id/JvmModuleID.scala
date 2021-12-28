package com.logicovercode.bsbt.module_id

import sbt._
import sbt.librarymanagement.MavenRepository

case class JvmModuleID(
                          moduleID: ModuleID,
                          resolverForThisModuleId: Option[MavenRepository] = None
                        ) {

    def from(mavenRepoResolver: Option[MavenRepository]): JvmModuleID = {
      new JvmModuleID(moduleID, mavenRepoResolver) {}
    }

    def %(configuration : Configuration) : JvmModuleID = {
      JvmModuleID(this.moduleID.withConfigurations(None) % configuration)
    }
  }