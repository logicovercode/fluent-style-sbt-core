name := "fluent-style-sbt-core"

version := "0.0.417"

sbtPlugin := true

libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.2" % Test
libraryDependencies ++= Seq(
  "com.whisk" %% "docker-testkit-impl-docker-java" % "0.9.9" excludeAll ( ExclusionRule("com.github.docker-java") ),
  "com.whisk" %% "docker-testkit-impl-spotify" % "0.9.9",
  //"javax.activation" % "activation" % "1.1.1",
  "com.github.docker-java" % "docker-java" % "3.2.7",
  "com.github.docker-java" % "docker-java-transport-httpclient5" % "3.2.7"
)

// https://mvnrepository.com/artifact/com.github.pathikrit/better-files
libraryDependencies += "com.github.pathikrit" %% "better-files" % "3.9.1"

val testDirs = List( /*"core-spec"*/ "docker-spec")
unmanagedSourceDirectories in Test ++= testDirs.map(dir => (baseDirectory in Test).value / dir)

val projectSourceDirs = List("core", "plugin", "publishing", "docker", "paths", "scala-fmt", "os")
unmanagedSourceDirectories in Compile ++= projectSourceDirs.map(dir => (baseDirectory in Compile).value / dir)

organization := "org.logicovercode"

val cto = Developer(
  "cto",
  "cto",
  "oss@logicovercode.com",
  url("https://github.com/logicovercode")
)
developers := List(cto)

homepage := Some(
  url("https://github.com/logicovercode/FluentStyleSbtCorePlugin")
)
scmInfo := Some(
  ScmInfo(
    url("https://github.com/logicovercode/FluentStyleSbtCorePlugin"),
    "git@github.com:logicovercode/FluentStyleSbtCorePlugin.git"
  )
)

licenses += ("MIT", url("https://opensource.org/licenses/MIT"))

//publishing related settings

crossPaths := false

publishMavenStyle := true

publishTo := Some(Opts.resolver.sonatypeStaging)

//below is not yet working as expected (exploring ...)
publishConfiguration := publishConfiguration.value.withOverwrite(true)
publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)
