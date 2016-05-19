name :="SBT Playground"

version :="1.0"

//gitHeadCommitSha now confined to build. Not the project
//so now it's shared across all projects
//this will be the fallback if SBT doesn't find it in any project
val gitHeadCommitSha = taskKey[String]("Determines the current git commit SHA")
gitHeadCommitSha in ThisBuild := Process("git rev-parse HEAD").lines.head

val makeVersionProperties = taskKey[Seq[File]]("Creates a version.properties file we can find at runtime.")
makeVersionProperties := {
  val propFile = (resourceManaged in Compile).value / "version.properties"
  val content = "version=%s" format (gitHeadCommitSha.value)
  IO.write(propFile, content)
  Seq(propFile)
}

libraryDependencies += "org.specs2" % "specs2_2.10" % "1.14" % "test"
libraryDependencies += "org.pegdown" % "pegdown" % "1.0.2" % "test"
testOptions in Test += Tests.Argument("html") //specs2 generates html reports

libraryDependencies += "junit" % "junit" % "4.11" % "test"

/*lazy val common = ( //new sub-project with it's own settings
		Project("common",file("common")). //relative to the base dir. Sources should be in common/src/
		settings(
				libraryDependencies += "org.specs2" % "specs2_2.10" % "1.14" % "test"
			)
	)
 
lazy val analytics = ( //new-project 
 Project("analytics", file("analytics"))
 dependsOn(common)
 settings()
)

lazy val website = ( //new sub-project
 Project("website", file("website"))
 dependsOn(common)
 settings()
)*/

lazy val SbtPlayground = Project("SbtPlayground",file("."))
	.settings( Defaults.itSettings : _*) //adds integration test
	.configs(IntegrationTest) //adds integration test //Default to SBT
	/*
		By default, this configuration uses the directory src/it, 
		so it will look for Scala sources in src/it/scala, and resources in src/it/resources. 
		It compiles classes to target/ it-classes.
	*/

//can access sub-project tasks like so: common/clean

//helper function to create new sub-projects
def helperFunction(name: String): Project = (
  Project(name, file(name)).
  settings( //default settings for all projects using this 
    	version := "1.0",
    	organization := "com.preownedkittens",
    	libraryDependencies += "org.specs2" % "specs2_2.10" % "1.14" % "test"
	)
)

lazy val randomProject = {
	helperFunction("randomProject")
	.settings(
		libraryDependencies += "org.specs2" % "specs2_2.10" % "1.14" % "test"
		)
}