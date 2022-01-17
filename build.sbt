//basic info starts here
name := "fluent-style-sbt-core"

version := "0.0.424"

organization := "com.logicovercode"

scalaVersion := "2.12.15"

sbtPlugin := true

licenses += ("MIT", url("https://opensource.org/licenses/MIT"))
//basic info ends here

//dependencies info starts here
libraryDependencies ++= Seq(
  "com.github.pathikrit" %% "better-files" % "3.9.1",
  "com.logicovercode" %% "docker-core" % "0.0.004",
  "com.logicovercode" %% "fsbt-adts" % "0.0.001",
  "org.scalatest" %% "scalatest" % "3.2.10" % Test,
)
addDependencyTreePlugin
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.6")
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.9.10")
addSbtPlugin("io.github.davidmweber" % "flyway-sbt" % "6.5.0")
//dependencies info ends here

//directories starts here
val testDirs = List( /*"core-spec"*/ "docker-spec")
Test / unmanagedSourceDirectories ++= testDirs.map(dir => (Test / baseDirectory).value / dir)

val projectSourceDirs = List("build-core", "build-language-support/java", "build-language-support/scala",
  "build-scala", "build-java", "sbt-project", "plugin",
  "publishing", "scala-fmt",
  "docker", "docker-whisk/core", "docker-whisk/impl-docker-java")
Compile / unmanagedSourceDirectories ++= projectSourceDirs.map(dir => (Compile / baseDirectory).value / dir)
//directories ends here

//scm info starts here
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
//scm info ends here

Compile / compile / javacOptions ++= Seq("-source", "1.8", "-target", "1.8")
Compile / compile / scalacOptions ++= Seq("-release", "8", "-target:jvm-1.8")


//publishing related settings starts here
publishTo := Some(Opts.resolver.sonatypeStaging)
publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true)
//publishing related settings ends here

