name := """watforum"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "org.webjars" %% "webjars-play" % "2.4.0-1",
  "org.webjars" % "bootstrap" % "3.3.7-1",
  "org.ocpsoft.prettytime" % "prettytime" % "3.2.7.Final",
  "org.postgresql" % "postgresql" % "9.4-1206-jdbc42"
)

/*changed to false because of hanging application reloading*/
fork in run := false

includeFilter in (Assets, LessKeys.less) := "footer.less"
