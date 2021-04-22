package org.logicovercode.bsbt.core.model

import sbt.librarymanagement.{MavenRepository, ModuleID}

case class JvmModuleID(
    moduleID: ModuleID,
    resolverForThisModuleId: Option[MavenRepository] = None
) {

  def from(mavenRepoResolver: Option[MavenRepository]): JvmModuleID = {
    new JvmModuleID(moduleID, mavenRepoResolver) {}
  }
}
