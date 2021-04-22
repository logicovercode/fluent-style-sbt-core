package org.mogli.baseplugin.core.model

import org.scalatest.FlatSpecLike
import org.scalatest.matchers.should.Matchers
import sbt._
import sbt.librarymanagement.Configurations.{Compile, Provided, Test}

class ModuleSpec extends FlatSpecLike with Matchers{

  val mId = "org.scalatest" %% "scalatest" % "3.1.2"
  val jmId = JvmModuleID(mId)(Option(Provided))

  mId.toString() should "return true" in {
    isModuleId(mId) should be(true)
  }

  jmId.toString() should "return false" in {
    isModuleId(jmId) should be(false)
  }

  "any other serializable" should "throw RuntimeException" in {
    class Abc extends Serializable
    intercept[RuntimeException]( isModuleId(new Abc) )
  }

  "splitting collection of dependencies" should "work" in {
    val set : Set[Serializable] = Set(mId, jmId)
    //moduleIds(set) should be( Set(mId) )
    jvmModuleIds(set) should be( Set(jmId) )
  }

  "non scoped module id" should "have None configuration" in {
    mId.configurations should be ( None )
  }

  "moduleID" should "be extracted from scoped ModuleID without scope" in {
    val providedModuleId = "org.scalatest" %% "scalatest" % "3.1.2" % Provided
    providedModuleId.organization should be("org.scalatest")
    providedModuleId.name should be("scalatest")
    providedModuleId.revision should be ("3.1.2")
    val nonScopedModuleId = providedModuleId.organization %% providedModuleId.name % providedModuleId.revision
    nonScopedModuleId.configurations should be (None)
  }

  "scoped module id" should "have string configuration for corresponding moduleId Configuration" in {
    val testModuleId = "org.scalatest" %% "scalatest" % "3.1.2" % Test
    testModuleId.configurations should be ( Option("test") )

    val providedModuleId = "org.scalatest" %% "scalatest" % "3.1.2" % Provided
    providedModuleId.configurations should be ( Option("provided") )

    val pomOnlyModuleID = "org.springframework.boot" % "spring-boot-starter-parent" % "2.2.5.RELEASE" pomOnly()
    pomOnlyModuleID.configurations should be( None )
  }
}
