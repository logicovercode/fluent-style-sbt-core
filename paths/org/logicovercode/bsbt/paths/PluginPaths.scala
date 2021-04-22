package org.logicovercode.bsbt.paths

import org.logicovercode.bsbt.os.OsFunctions.isWindowsCategoryOs

trait PluginPaths  {

  val HOME = isWindowsCategoryOs match {
    case true => sys.env("USERPROFILE")
    case false    => sys.env("HOME")
  }
}
