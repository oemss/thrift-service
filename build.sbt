import com.twitter.scrooge.ScroogeSBT
import sbt.Keys._
import sbt._
name := "thrift"

version := "0.1"

scalaVersion := "2.11.7"

scalacOptions ++= Seq("-Xmax-classfile-name", "100")

libraryDependencies ++= Seq(
  "org.apache.thrift" % "libthrift" % "0.8.0",
  "com.twitter"        %% "finagle-thrift" % "6.33.0",
  "com.twitter" %% "scrooge-core" % "3.16.3",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "org.scalacheck" %% "scalacheck" % "1.12.2" % "test",
  "org.mockito"    %  "mockito-all"      % "1.9.5"  % "test",
  "org.scalamock"     %% "scalamock-scalatest-support"  % "3.2.2"         % "test"

)
concurrentRestrictions in Global += Tags.limit(Tags.Test, 1)

com.twitter.scrooge.ScroogeSBT.newSettings