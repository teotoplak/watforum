name := """watforum2"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

/*defining 1) group, 2) artifact and 3) revision (for dependencies)*/
libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "org.webjars" %% "webjars-play" % "2.4.0-1",
  "org.webjars" % "bootstrap" % "3.3.7-1",
  "org.ocpsoft.prettytime" % "prettytime" % "3.2.7.Final",
  "org.pac4j" % "play-pac4j" % "3.0.0-RC2-SNAPSHOT",
  "org.pac4j" % "pac4j-http" % "2.0.0-RC2-SNAPSHOT",
  "org.pac4j" % "pac4j-oauth" % "2.0.0-RC2-SNAPSHOT"
)

/*changed to false because of hanging application reloading*/
fork in run := false

includeFilter in (Assets, LessKeys.less) := "footer.less"

//resolvers ++= Seq(Resolver.mavenLocal) this should work later when the release RC's !!
resolvers ++= Seq(Resolver.mavenLocal, "Sonatype snapshots repository" at "https://oss.sonatype.org/content/repositories/snapshots/")

routesGenerator := InjectedRoutesGenerator