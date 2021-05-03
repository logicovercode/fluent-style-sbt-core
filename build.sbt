name := "fluent-style-sbt-core"

version := "0.0.417"

sbtPlugin := true

libraryDependencies += "org.scalatest" %% "scalatest" % "3.1.2" % Test
libraryDependencies ++= Seq(
  "com.whisk" %% "docker-testkit-impl-docker-java" % "0.9.9" excludeAll ( ExclusionRule("com.github.docker-java") ),
  //"javax.activation" % "activation" % "1.1.1",
  "com.github.docker-java" % "docker-java" % "3.2.7",
  "com.github.docker-java" % "docker-java-transport-httpclient5" % "3.2.7"
)

// https://mvnrepository.com/artifact/com.github.pathikrit/better-files
libraryDependencies += "com.github.pathikrit" %% "better-files" % "3.9.1"

val testDirs = List( /*"core-spec"*/ "docker-spec")
Test / unmanagedSourceDirectories ++= testDirs.map(dir => (Test / baseDirectory).value / dir)

val projectSourceDirs = List("build-core", "build-language-support/java", "build-language-support/scala", "build-scala", "build-java", "project-settings", "plugin", "publishing", "docker", "paths", "scala-fmt", "os")
Compile / unmanagedSourceDirectories ++= projectSourceDirs.map(dir => (Compile / baseDirectory).value / dir)

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
