
lazy val root = (project in file("."))
  .settings(
    scalaVersion := "2.12.9",


    libraryDependencies ++= Seq(
      "junit" % "junit" % "4.12" % Test,

      "com.typesafe.akka"    %% "akka-actor-typed"            % "2.6.9",
      "com.typesafe.akka"    %% "akka-actor"                  % "2.6.9",
      "com.typesafe.akka"    %% "akka-testkit"                % "2.6.9"      % "test",
      "com.typesafe.akka"    %% "akka-actor-testkit-typed"    % "2.6.9"      % "it,test",

      "org.scalactic"        %% "scalactic"                   % "3.2.0",
      "org.scalacheck"       %% "scalacheck"                  % "1.14.0"     % "it,test",
      "org.scalatest"        %% "scalatest"                   % "3.2.0"      % "test",
      "org.scalatest"        %% "scalatest-wordspec"          % "3.2.0"      % Test,
      "org.scalatestplus"    %% "scalacheck-1-14"             % "3.1.0.1"    % "it,test",
      "org.scalatestplus"    %% "scalatestplus-mockito"       % "1.0.0-M2"   % "it,test",
      "org.scalatestplus"    %% "mockito-3-4"                 % "3.2.2.0"    % "test",

      "org.mockito"          %% "mockito-scala"               % "1.10.2"     % "it,test"
    ),
    crossPaths := false,
    Test / parallelExecution := false
  )

