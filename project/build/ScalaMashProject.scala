import sbt._

class ScalaMashProject(info: ProjectInfo) extends ParentProject(info)
{
	 	
	def url = new java.net.URL("http://bizondemand.biz/artifacts")
	val bizOnDemand = Resolver.url("BizOnDemand", url)
			
	val stdLibraryDependencies = Set(
		"junit" % "junit" % "4.5" % "test->default",
		"org.scala-tools.testing" % "specs" % "1.6.1" % "test->default"	,
		"org.slf4j" % "slf4j-log4j12" % "1.4.1",
		"org.mockito" % "mockito-all" % "1.8.1" % "test->default",
		"joda-time" % "joda-time" % "1.6",		
		"bizondemand" %% "utils" % "20100716.1"
	) ++ super.libraryDependencies	
		
			
	lazy val freshbooks = project("freshbooks-api", "Freshbooks", new ApiProject(_))	
	
	lazy val highrise = project("highrise-api", "Highrise", new ApiProject(_))	
	
	lazy val rest = project("rest-core", "Rest Core", new CoreProject(_))	
	
	lazy val shopify = project("shopify-api", "Shopify", new ApiProject(_))
	
  	val publishTo = Resolver.ssh("BizOnDemand", "bizondemand.biz", "/var/local/artifacts/release/") 

	class ApiProject(info:ProjectInfo) extends DefaultProject(info) {
		
		override def libraryDependencies = stdLibraryDependencies
		
		lazy val embededCore = project("rest-core", "Rest Core", rest)
		
	}
	
	class CoreProject(info: ProjectInfo) extends DefaultWebProject(info) {
		override def libraryDependencies = stdLibraryDependencies ++ Set("org.apache.httpcomponents" % "httpclient" % "4.1-alpha2")
		
		
	}
}
