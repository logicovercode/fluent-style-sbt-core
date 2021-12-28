package com.logicovercode.bsbt.paths

import com.logicovercode.bsbt.os.OsFunctions.isWindowsCategoryOs

trait PluginPathSettings  {

  val HOME = isWindowsCategoryOs match {
    case true => sys.env("USERPROFILE")
    case false    => sys.env("HOME")
  }
}
