package org.mogli.baseplugin.core.model

import org.logicovercode.bsbt.core.model.JvmModuleID
import org.scalatest.flatspec.AsyncFlatSpecLike
import org.scalatest.matchers.should.Matchers
import sbt._

class JvmModuleIdSpec extends AsyncFlatSpecLike with Matchers {

  {
    implicit val validImplicitModuleScopes = Set(Option(Compile), Option(Provided), None)

    "JvmModuleID modules" should "convert ModuleID without scope to JvmModuleId with None package scope" in {
      val moduleID = "org.scalatest" %% "scalatest" % "3.1.2"
      val jvmModuleID = JvmModuleID(moduleID)
      jvmModuleID should be(JvmModuleID(moduleID)(None)(None))
    }

    it should "convert ModuleID with Compile scope to JvmModuleId with Compile package scope" in {
      val moduleID = "org.scalatest" %% "scalatest" % "3.1.2" % Compile
      val jvmModuleID = JvmModuleID(moduleID)
      jvmModuleID should be(JvmModuleID(moduleID)(Option(Compile))(None))
    }

    it should "convert ModuleID with Provided scope to JvmModuleId with Provided package scope" in {
      val moduleID = "org.scalatest" %% "scalatest" % "3.1.2" % Provided
      val jvmModuleID = JvmModuleID(moduleID)
      jvmModuleID should be(JvmModuleID(moduleID)(Option(Provided))(None))
    }

    it should "throw exception when converting ModuleID with Test scope to JvmModuleId" in {
      val moduleID = "org.scalatest" %% "scalatest" % "3.1.2" % Test
      JvmModuleID(moduleID)
      succeed
      //val ex = intercept[RuntimeException] ( JvmModuleID(moduleID) )
      //ex shouldBe a[RuntimeException]
    }
  }

  {
    implicit val validImplicitModuleScopes = Set(Option(Test), None)

    "JvmModuleID testModules" should "convert ModuleID without scope to JvmModuleId with None package scope" in {
      val moduleID = "org.scalatest" %% "scalatest" % "3.1.2"
      val jvmModuleID = JvmModuleID(moduleID)
      jvmModuleID should be(JvmModuleID(moduleID)(None)(None))
    }

    it should "convert ModuleID with Test scope to JvmModuleId with Test package scope" in {
      val moduleID = "org.scalatest" %% "scalatest" % "3.1.2" % Test
      val jvmModuleID = JvmModuleID(moduleID)
      jvmModuleID should be(JvmModuleID(moduleID)(Option(Test))(None))
    }

    it should "throw exception when converting ModuleID with Compile scope to JvmModuleId" in {
      val moduleID = "org.scalatest" %% "scalatest" % "3.1.2" % Compile
      JvmModuleID(moduleID)
      succeed
      //val ex = intercept[RuntimeException] ( JvmModuleID(moduleID) )
      //ex shouldBe a[RuntimeException]
    }
  }
}