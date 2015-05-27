name := "artymonkeys_play"

version := "1.1.0.0-SNAPSHOT"

lazy val `artymonkeys_play` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(jdbc, anorm, cache, ws,
  "javax.mail" % "mail" % "1.4.1",
  "com.typesafe.play" % "play-mailer_2.11" % "2.4.0",
  "org.mongodb" %% "casbah" % "2.8.1",
  "be.objectify" %% "deadbolt-scala" % "2.3.3",
  "io.jsonwebtoken" % "jjwt" % "0.5",
  "com.google.guava" % "guava" % "18.0",
    "com.github.simplyscala" %% "scalatest-embedmongo" % "0.2.2" % "test"
)

unmanagedResourceDirectories in Test <+= baseDirectory(_ / "target/web/public/test")

