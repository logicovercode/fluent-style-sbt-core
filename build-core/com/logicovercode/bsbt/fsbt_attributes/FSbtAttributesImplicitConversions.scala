package com.logicovercode.bsbt.fsbt_attributes

import com.logicovercode.fsbt.commons.{FSbtLicense, FSbtResolver}
import sbt._

trait FSbtAttributesImplicitConversions {

  implicit def fSbtLicensesToLicenses(fSbtLicense : FSbtLicense): (String, URL) = {
    (fSbtLicense.name, new URL(fSbtLicense.url))
  }

  implicit def fSbtResolversToResolvers(fSbtResolvers : Seq[FSbtResolver]): Seq[MavenRepository] = {
    fSbtResolvers.map(fSbtResolver => fSbtResolver.name at fSbtResolver.url)
  }
}
