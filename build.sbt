name := "fluent-style-sbt-core"

version := "0.0.423"

sbtPlugin := true

libraryDependencies ++= Seq(
  "com.github.pathikrit" %% "better-files" % "3.9.1",
  "com.logicovercode" %% "docker-core" % "0.0.004",
  "com.logicovercode" %% "fsbt-adts" % "0.0.001",
  "org.scalatest" %% "scalatest" % "3.2.10" % Test,
)

addSbtPlugin("io.github.davidmweber" % "flyway-sbt" % "6.5.0")

val testDirs = List( /*"core-spec"*/ "docker-spec")
Test / unmanagedSourceDirectories ++= testDirs.map(dir => (Test / baseDirectory).value / dir)

val projectSourceDirs = List("build-core", "build-language-support/java", "build-language-support/scala",
  "build-scala", "build-java", "sbt-project", "plugin",
  "publishing", "paths", "scala-fmt",
  "docker", "docker-whisk/core", "docker-whisk/impl-docker-java")
Compile / unmanagedSourceDirectories ++= projectSourceDirs.map(dir => (Compile / baseDirectory).value / dir)

organization := "com.logicovercode"

val techLead = Developer(
  "techLead",
  "techLead",
  "techlead@logicovercode.com",
  url("https://github.com/logicovercode")
)
developers := List(techLead)

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

//crossPaths := false
publishMavenStyle := true

publishTo := Some(Opts.resolver.sonatypeStaging)

//below is not yet working as expected (exploring ...)
publishConfiguration := publishConfiguration.value.withOverwrite(true)
publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)

