ThisBuild / name := "azure-storage-cdn-mock"

ThisBuild / organization := "janjaali"

ThisBuild / version := "0.1.0"

ThisBuild / licenses := Seq("MIT License" -> url("https://opensource.org/licenses/MIT"))

ThisBuild / scalaVersion := "2.13.1"

ThisBuild / scalacOptions := Seq(
  "-deprecation",                   // Emit warning and location for usages of deprecated APIs.
  "-encoding", "utf-8",             // Specify character encoding used by source files.
  "-explaintypes",                  // Explain type errors in more detail.
  "-feature",                       // Emit warning and location for usages of features that should be imported explicitly.
  "-language:existentials",         // Existential types (besides wildcard types) can be written and inferred.
  "-language:higherKinds",          // Allow higher-kinded types.
  "-language:implicitConversions",  // Allow definition of implicit functions called views.
  "-Xcheckinit",                    // Wrap field accessors to throw an exception on uninitialized access.
  "-Xlint:adapted-args",            // Warn if an argument list is modified to match the receiver.
  "-Xlint:nullary-unit",            // Warn when nullary methods return Unit.
  "-Xlint:inaccessible",            // Warn about inaccessible types in method signatures.
  "-Xlint:nullary-override",        // Warn when non-nullary def f() overrides nullary def f.
  "-Xlint:infer-any",               // Warn when a type argument is inferred to be Any.
  "-Xlint:missing-interpolator",    // A string literal appears to be missing an interpolator id.
  "-Xlint:doc-detached",            // A Scaladoc comment appears to be detached from its element.
  "-Xlint:private-shadow",          // A private field (or class parameter) shadows a superclass field.
  "-Xlint:type-parameter-shadow",   // A local type parameter shadows a type already in scope.
  "-Xlint:poly-implicit-overload",  // Parameterized overloaded implicit methods are not visible as view bounds.
  "-Xlint:option-implicit",         // Option.apply used implicit view.
  "-Xlint:delayedinit-select",      // Selecting member of DelayedInit.
  "-Xlint:package-object-classes",  // Class or object defined in package object.
  "-Xlint:stars-align",             // Pattern sequence wildcard must align with sequence component.
  "-Xlint:constant",                // Evaluation of a constant arithmetic expression results in an error.
  "-Xlint:unused",                  // Enable -Wunused:imports,privates,locals,implicits.
  "-Xlint:nonlocal-return",         // A return statement used an exception for flow control.
  "-Xlint:deprecation",             // Enable linted deprecations.
  "-Ywarn-dead-code",               // Warn when dead code is identified.
  "-Ywarn-extra-implicit",          // Warn when more than one implicit parameter section is defined.
  "-Ywarn-numeric-widen",           // Warn when numerics are widened.
  "-Ywarn-octal-literal",           // Warn on obsolete octal syntax.
  "-Wunused:imports",               // Warn if an import selector is not referenced.
  "-Wunused:patvars",               // Warn if a variable bound in a pattern is unused.
  "-Wunused:privates",              // Warn if a private member is unused.
  "-Wunused:locals",                // Warn if a local definition is unused.
  "-Wunused:params"
)

lazy val app = (project in file("app"))
  .settings(
    name := "app",

    mainClass in Compile := Some("janjaali.AzureStorageCdnMock"),

    libraryDependencies ++= Seq(
      // logging
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",

      // tests
      "org.scalatest" %% "scalatest" % "3.1.0" % Test,

      // mocking
      "org.scalamock" %% "scalamock" % "4.4.0" % Test,

      // http
      "com.typesafe.akka" %% "akka-http"   % "10.1.11",
      "com.typesafe.akka" %% "akka-http-testkit" % "10.1.11" % Test,

      // stream processing
      "com.typesafe.akka" %% "akka-stream" % "2.6.4",
      "com.typesafe.akka" %% "akka-stream-testkit" % "2.6.4" % Test,

      // azure storage
      "com.github.janjaali" %% "azure-storage-sdk-wrapper-for-scala" % "0.1.0-SNAPSHOT-2"
    ),

    packageName in Docker := "janjaali/azure-storage-cdn-mock",

    dockerExposedPorts ++= Seq(8080)
  )
  .enablePlugins(JavaAppPackaging)
