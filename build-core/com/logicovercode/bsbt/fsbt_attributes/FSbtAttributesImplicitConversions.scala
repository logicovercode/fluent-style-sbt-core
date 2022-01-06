package com.logicovercode.bsbt.fsbt_attributes

import org.apache.ivy.core.module.descriptor.License
import sbt._

trait FSbtAttributesImplicitConversions {

  implicit def LicenseToTuple(license : License): (String, URL) = {
    (license.getName, new URL(license.getUrl))
  }
}
