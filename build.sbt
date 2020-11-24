
lazy val root = (project in file("."))
  .settings(
    scalaVersion := "2.12.9",
    mainClass in (Compile, packageBin) := Some("averageJoes.view.View"),
    libraryDependencies ++= Seq(
      "junit" % "junit" % "4.12" % Test,

      "com.typesafe.akka"    %% "akka-actor-typed"            % "2.6.9",
      "com.typesafe.akka"    %% "akka-actor"                  % "2.6.9",
      "com.typesafe.akka"    %% "akka-testkit"                % "2.6.9"      % "test",
      "com.typesafe.akka"    %% "akka-actor-testkit-typed"    % "2.6.9"      % "test",

      "org.scalactic"        %% "scalactic"                   % "3.2.0",
      "org.scalatest"        %% "scalatest"                   % "3.2.0"      % Test,
      "org.scalatest"        %% "scalatest-wordspec"          % "3.2.0"      % Test,


      "org.mockito"          %% "mockito-scala"               % "1.10.2"     % "test",

      "com.typesafe.akka"    %% "akka-cluster-tools"          % "2.6.9",
      "com.typesafe.akka"    %% "akka-cluster-typed"          % "2.6.9",

      "org.slf4j"            % "slf4j-simple"                 % "1.7.30" ,

      "org.scala-lang.modules" % "scala-xml_2.12" % "1.0.6",

      "net.liftweb" %% "lift-json" % "3.1.0",

      "com.typesafe.play" %% "play-json" % "2.9.1"

),
    fork in run := true,
    crossPaths := false,
    Test / parallelExecution := false
  )

