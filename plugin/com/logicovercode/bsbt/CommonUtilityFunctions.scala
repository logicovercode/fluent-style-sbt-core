package com.logicovercode.bsbt

import better.files.File

trait CommonUtilityFunctions {

  def mapFromConfFile(confFile: File): Map[String, String] = {
    val keyValuePairs = confFile.lines.map(_.split("="))
    val conf = keyValuePairs collect { case Array(key, value) if (!key.trim.isEmpty) => key.trim -> value }
    conf.toMap
  }
}
