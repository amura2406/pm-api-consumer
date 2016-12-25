name := "pm-api-consumer"
organization := "com.pocketmath"

version := "1.0"

scalaVersion := "2.11.8"

fork in run := true

mainClass in (Compile, run) := Some("com.pocketmath.bot.Main")

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.jcenterRepo,
  "Twitter Maven" at "https://maven.twttr.com",
  "jitpack" at "https://jitpack.io",
  "Sonatype OSS Releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2")

lazy val versions = new {
  val scalaGuice          = "4.1.0"
  val typesafeConfig      = "1.3.0"
  val typesafeConfigGuice = "0.0.3"
  val finagle             = "6.40.0"
  val circe               = "0.6.1"
}

libraryDependencies ++= Seq(
  "net.codingwell" %% "scala-guice" % versions.scalaGuice,
  "com.typesafe" % "config" % versions.typesafeConfig,
  "com.github.racc" % "typesafeconfig-guice" % versions.typesafeConfigGuice,
  "com.twitter" % "finagle-http_2.11" % versions.finagle,
  "io.circe" %% "circe-core" % versions.circe,
  "io.circe" %% "circe-generic" % versions.circe,
  "io.circe" %% "circe-parser" % versions.circe
)
    