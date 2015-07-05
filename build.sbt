name := "hotel"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.7"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  cache,
  filters,
  "org.apache.commons"      %  "commons-email"               % "1.3.2" 
)

libraryDependencies ++= Seq (
	"org.webjars" %% "webjars-play" % "2.3.0-2",
	"org.webjars" % "bootstrap" % "3.3.1",
	"org.webjars" % "jquery" % "2.1.3",
	"org.webjars" % "jquery-ui" % "1.11.2",
	"org.webjars" % "jquery-ui-themes" % "1.11.0",
	"org.webjars" % "jquery-ui-src" % "1.11.2"
)     


includeFilter in (Assets, LessKeys.less) := "*.less"

excludeFilter in (Assets, LessKeys.less) := "_*.less"

includeFilter in uglify := GlobFilter("javascripts/*.js")

pipelineStages := Seq(rjs, uglify, digest, gzip)
