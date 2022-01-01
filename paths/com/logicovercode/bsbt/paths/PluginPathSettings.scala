package com.logicovercode.bsbt.paths

import better.files.File
import com.logicovercode.wdocker.OsFunctions.isWindowsCategoryOs

trait PluginPathSettings  {

  val HOME = isWindowsCategoryOs() match {
    case true => File( sys.env("USERPROFILE") )
    case false    => File( sys.env("HOME") )
  }
}
