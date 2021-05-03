package org.logicovercode.bsbt.module_id

import sbt.librarymanagement.{MavenRepository, ModuleID}

trait ModuleIDImplicitConversions {

  implicit def moduleIdToSeqJvmModuleID(moduleID: ModuleID): Seq[JvmModuleID] = Seq(JvmModuleID(moduleID))

  implicit def seqOfModuleIdToSeqJvmModuleID(moduleIDSeq: Seq[ModuleID]): Seq[JvmModuleID] = moduleIDSeq.map(JvmModuleID(_))

  implicit def jvmModuleIdToSeqJvmModuleID(jvmModuleID: JvmModuleID): Seq[JvmModuleID] = Seq(jvmModuleID)

  implicit class ModuleIdExtension(moduleID: ModuleID) {

    def fetchFrom(resolver: MavenRepository): Seq[JvmModuleID] = {
      Seq(JvmModuleID(moduleID, Option(resolver)))
    }
  }

}
