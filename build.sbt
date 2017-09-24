name := "introduction-akka-http-workshop"
organization := "io.github.jlprat"
version := "1.0.0"
scalaVersion := "2.12.3"

lazy val akkaHttpVersion = "10.0.10"

libraryDependencies ++= Seq(

  // akka http
  "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,

  // ftp
  "commons-net"        % "commons-net"          % "3.6",

  // testing
  "com.typesafe.akka" %% "akka-http-testkit"    % akkaHttpVersion % "test",
  "org.scalatest"     %% "scalatest"            % "3.0.4"     % "test",
)

testOptions += Tests.Argument(TestFrameworks.JUnit, "-v")
