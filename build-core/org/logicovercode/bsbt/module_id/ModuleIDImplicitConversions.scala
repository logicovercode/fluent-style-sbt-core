package org.logicovercode.bsbt.module_id

import sbt.Configuration
import sbt.librarymanagement.{MavenRepository, ModuleID}

trait ModuleIDImplicitConversions {

  implicit def moduleIdToSeqJvmModuleID(moduleID: ModuleID): Seq[JvmModuleID] = Seq(JvmModuleID(moduleID))

  implicit def seqOfModuleIdToSeqJvmModuleID(moduleIDSeq: Seq[ModuleID]): Seq[JvmModuleID] = moduleIDSeq.map(JvmModuleID(_))

  implicit def jvmModuleIdToSeqJvmModuleID(jvmModuleID: JvmModuleID): Seq[JvmModuleID] = Seq(jvmModuleID)

//  implicit class JvmModuleIdSeqExtension(jvmModuleIds : Seq[JvmModuleID]){
//    def %(configuration : Configuration) : Seq[JvmModuleID] = {
//      jvmModuleIds.map(_ % configuration)
//    }
//  }

  implicit class ModuleIdExtension(moduleID: ModuleID) {

    def fetchFrom(resolver: MavenRepository): Seq[JvmModuleID] = {
      Seq(JvmModuleID(moduleID, Option(resolver)))
    }
  }

}
