package org.logicovercode.bsbt.os

object OsFunctions {

  def currentOsOption = sys.props.get("os.name").map(_.toLowerCase)

  @Deprecated
  def isWindowsCategoryOs: Boolean = (for {
    osName <- sys.props.get("os.name")
    os_name = osName.toLowerCase
  } yield os_name.contains("win")).getOrElse(false)

  def isWindowsCategoryOs(os : Option[String]): Boolean = (for {
    osName <- os
    os_name = osName.toLowerCase
  } yield os_name.contains("win")).getOrElse(false)
}
