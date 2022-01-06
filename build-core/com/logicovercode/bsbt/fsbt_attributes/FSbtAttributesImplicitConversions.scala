package com.logicovercode.bsbt.fsbt_attributes

import com.logicovercode.fsbt.commons.{FSbtLicense, FSbtResolver}
import org.apache.ivy.core.module.descriptor.License
import sbt._

trait FSbtAttributesImplicitConversions {

  implicit def fSbtLicenseToTuple(fSbtLicense : FSbtLicense): (String, URL) = {
    (fSbtLicense.name, new URL(fSbtLicense.url))
  }

  implicit def fSbtLicenseToLicense(fSbtLicense : FSbtLicense): License = {
    new License(fSbtLicense.name, fSbtLicense.url)
  }

  implicit def fSbtResolversToResolvers(fSbtResolvers : Seq[FSbtResolver]): Seq[MavenRepository] = {
    fSbtResolvers.map(fSbtResolver => fSbtResolver.name at fSbtResolver.url)
  }
}
