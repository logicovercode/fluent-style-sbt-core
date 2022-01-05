package com.logicovercode.bsbt.module_id

import com.logicovercode.fsbt.commons.FSbtModuleId
import sbt.librarymanagement.{MavenRepository, ModuleID}
import sbt._

trait ModuleIDImplicitConversions {

  implicit def moduleIdToSeqJvmModuleID(moduleID: ModuleID): Seq[JvmModuleID] = Seq(JvmModuleID(moduleID))

  implicit def seqOfModuleIdToSeqJvmModuleID(moduleIDSeq: Seq[ModuleID]): Seq[JvmModuleID] = moduleIDSeq.map(JvmModuleID(_))

  implicit def jvmModuleIdToSeqJvmModuleID(jvmModuleID: JvmModuleID): Seq[JvmModuleID] = Seq(jvmModuleID)

  implicit class ModuleIdExtension(moduleID: ModuleID) {

    def fetchFrom(resolver: MavenRepository): Seq[JvmModuleID] = {
      Seq(JvmModuleID(moduleID, Option(resolver)))
    }
  }

  private def moduleIdFromFSbtModuleId(fSbtModuleId: FSbtModuleId) : ModuleID = {

    import fSbtModuleId._

    (crossPath, onlyPom) match {
      case (true, true) => organization %% artifactId % version.version pomOnly()
      case (true, false) => organization %% artifactId % version.version
      case (false, true) => organization % artifactId % version.version pomOnly()
      case (false, false) => organization % artifactId % version.version
    }
  }

  implicit def fSbtModuleIdToSeqJvmModuleID(fSbtModuleId: FSbtModuleId): Seq[JvmModuleID] = {
    val moduleID = moduleIdFromFSbtModuleId(fSbtModuleId)
    Seq(JvmModuleID(moduleID))
  }

  implicit def seqOfFSbtModuleIdToSeqJvmModuleID(fSbtModuleIDSeq: Seq[FSbtModuleId]): Seq[JvmModuleID] = {
    val moduleIDSeq = fSbtModuleIDSeq.map( moduleIdFromFSbtModuleId )
    moduleIDSeq.map(JvmModuleID(_))
  }

  implicit class FSbtModuleIdExtension(fSbtModuleId: FSbtModuleId) {

    def fetchFrom(resolver: MavenRepository): Seq[JvmModuleID] = {
      val moduleID = moduleIdFromFSbtModuleId(fSbtModuleId)
      Seq(JvmModuleID(moduleID, Option(resolver)))
    }
  }
}
