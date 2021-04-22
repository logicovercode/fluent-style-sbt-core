package org.logicovercode.bsbt.core

import org.logicovercode.bsbt.core.model.JvmModuleID
import sbt.librarymanagement.{Configuration, MavenRepository, ModuleID}

trait ModuleIDSettings {

  implicit def moduleIdToSeqJvmModuleID(moduleID: ModuleID): Seq[JvmModuleID] = Seq(JvmModuleID(moduleID))

  implicit def seqOfModuleIdToSeqJvmModuleID(moduleIDSeq: Seq[ModuleID]): Seq[JvmModuleID] = moduleIDSeq.map( JvmModuleID(_) )


  implicit def jvmModuleIdToSeqJvmModuleID(jvmModuleID: JvmModuleID): Seq[JvmModuleID] = Seq(jvmModuleID)

  implicit class ModuleIdExtension(moduleID: ModuleID) {

    def fetchFrom(resolver: MavenRepository): Seq[JvmModuleID] = {
      Seq(JvmModuleID(moduleID, Option(resolver)))
    }
  }

}
