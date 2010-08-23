import sbt._

class ScalaMashProject(info: ProjectInfo) extends ParentProject(info)
{
	 		
	val bizOnDemand_libs_Releases     =  "libs-releases-local"   at 
			"http://bizondemand.biz:8081/artifactory/libs-releases-local"
  val bizOnDemand_libs_Snapshots    = "libs-snapshots-local"   at 
			"http://bizondemand.biz:8081/artifactory/libs-snapshots-local"
  val bizOnDemand_plugins_Releases  = "plugins-releases-local" at 
			"http://bizondemand.biz:8081/artifactory/plugins-releases-local"
  val bizOnDemand_plugins_Snapshots = "plugins-snaphots-local" at 
			"http://bizondemand.biz:8081/artifactory/plugins-snapshots-local"
  
	val scalatoolsSnapshot = 
    "Scala Tools Snapshot" at "http://scala-tools.org/repo-snapshots/"

	override def managedStyle=ManagedStyle.Maven
	Credentials(Path.userHome / ".ivy2" / ".credentials", log)
  val publishTo = "Biz On Demand Artifacts" at "http://bizondemand.biz:8081/artifactory/libs-releases-local"

			
	val stdLibraryDependencies = Set(
		"junit" % "junit" % "4.5" % "test->default",
		"org.scala-tools.testing" % "specs" % "1.6.1" % "test->default"	,
		"org.slf4j" % "slf4j-log4j12" % "1.4.1",
		"org.mockito" % "mockito-all" % "1.8.1" % "test->default",
		"joda-time" % "joda-time" % "1.6",		
		"bizondemand" %% "utils" % "20100716.1"
	) ++ super.libraryDependencies	
		
			
	lazy val rest = project("rest-core", "Rest Core", new CoreProject(_))	
	
	lazy val freshbooks = project("freshbooks-api", "Freshbooks", new ApiProject(_),rest)	
	
	lazy val highrise = project("highrise-api", "Highrise", new ApiProject(_), rest)	
	
	lazy val shopify = project("shopify-api", "Shopify", new ApiProject(_), rest)

	class ApiProject(info:ProjectInfo) extends DefaultProject(info) {
		
		override def libraryDependencies = stdLibraryDependencies 
		
	}
	
	class CoreProject(info: ProjectInfo) extends DefaultProject(info) {
		override def libraryDependencies = stdLibraryDependencies ++ Set("org.apache.httpcomponents" % "httpclient" % "4.1-alpha2")
		
		
	}
	
}
