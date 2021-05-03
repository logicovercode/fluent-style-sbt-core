package org.mogli.baseplugin.core.model

import org.logicovercode.bsbt.build.model.JvmModuleID
import org.scalatest.flatspec.AsyncFlatSpecLike
import org.scalatest.matchers.should.Matchers
import sbt._

class JvmPackageScopeSpec extends AsyncFlatSpecLike with Matchers {

  val moduleScopes = Set(None, Option(Compile), Option(Provided), Option(Test))
  val moduleID = "org.scalatest" %% "scalatest" % "3.1.2"

  val moduleIds = moduleScopes.flatten.map(sc => moduleID % sc) + moduleID

  for{
    mID <- moduleIds
  } yield {
    val jvmId = JvmModuleID(mID)
    jvmId.toString should " have same package scope and moduleID scope, when packageScope is not passed explicitly" in {
      val conf = jvmId.moduleID.configurations flatMap  (c => configuration(c))
      jvmId.packageScope should be(conf)
    }
  }

  for {
    mID <- moduleIds
    sc <- moduleScopes.flatten
    jvmId = JvmModuleID(mID)
  } yield {
    val jvmIdWithNewScope = jvmId(Option(sc))
    jvmId.toString should s" return $jvmIdWithNewScope, when packageScope($sc) is passed explicitly" in {

      val conf = jvmId.moduleID.configurations flatMap  (c => configuration(c))
      jvmIdWithNewScope.packageScope should be( Option(sc) )
    }
  }
}