name := "artymonkeys_play"

version := "1.0.0.1"

lazy val `artymonkeys_play` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq( jdbc , anorm , cache , ws,
"javax.mail" % "mail" % "1.4.1",
"com.typesafe.play" % "play-mailer_2.11" % "2.4.0"
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

