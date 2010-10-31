package scala_mash {
	package rest {

import java.net.URI
import xml.{XML, NodeSeq}

import scala.xml.parsing._

import scala.collection.jcl.Conversions._

import org.apache.http.HttpRequest
import org.apache.http.auth.{UsernamePasswordCredentials, AuthScope}
import org.apache.http.client.HttpClient
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.HttpResponse
import org.apache.http.entity.{StringEntity}
import org.apache.http.conn.scheme.{PlainSocketFactory, Scheme, SchemeRegistry}
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager
import org.apache.http.conn.ssl.SSLSocketFactory
import org.apache.http.client.methods._

import io.Source

import bizondemand.utils.logging.Log
import bizondemand.utils.models.internet.Url

/**The trait to inherit to add rest service functionality.
 * 
 *
 */


trait RestService extends Log{	
	
	lazy val maxRetries = 3
	

	def post(url: Url, username: Option[String], password: Option[String], request: NodeSeq ): HttpStatusCode = {
    	debug("RestService.post: Url: {} username: {} password: {} + request: {} ", url, username, password, request.toString)
    	val postMethod = new HttpPost(url.toString)
    	postMethod.setEntity(makeEntity(request))
    	makeRequest( postMethod, username, password)
    	
  	}	
   	

	def get( url: Url, username: Option[String], password: Option[String]): HttpStatusCode = {
    	debug("RestService.get: Url: {} username: {} password: {} ", url, username, password)
	    val getMethod = new HttpGet(url.toString)
    	makeRequest( getMethod, username, password)
	}

	def put(url: Url, username: Option[String], password: Option[String], thingToPut: NodeSeq): HttpStatusCode= {
    	debug("RestService: Url: {} username: {} password: {} thingToPut: {}", url, username, password, thingToPut.toString)
    	val putMethod = new HttpPut(url.toString)
    	putMethod.setEntity(makeEntity(thingToPut))
    	makeRequest(putMethod, username, password)
	}

	def delete(url: Url, username: Option[String], password: Option[String]) : HttpStatusCode= {
    	debug("RestService.delete: Url: {} username: {} password: {} ", url, username, password)
    	val deleteMethod = new HttpDelete(url.toString)
    	makeRequest(deleteMethod, username, password)
	}
	
  	
  	protected def makeRequest( method: HttpRequestBase, username: Option[String], password: Option[String]) : HttpStatusCode = {
  		debug("RestService.makeRequest: method: {}, username:{}, password:{}", method, username, password)
		val client = createConnectionManager
		username.map( loginId => 
			client.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY), new UsernamePasswordCredentials(loginId, password.getOrElse("")))
		)
		try {
			HttpStatusCode( client.execute(method))
		}finally {
			method.abort
		}
	}
	
	def createConnectionManager = new DefaultHttpClient(RestService.clientConnectionManager )


	protected def makeEntity(request: NodeSeq): StringEntity = {
    	val entity = new StringEntity(request.toString);
    	entity.setContentType("application/xml")
    	entity
  	}

	def convertResponseToXml( response :String):NodeSeq = {
		debug("convertResponseToXml: {}", response)
		ConstructingParser.fromSource(Source.fromString(response), true).document
	}
}



class RestException( httpStatusCode:HttpStatusCode) extends Exception {
	def httpStatus = httpStatusCode
	override def toString = "RestException %s".format(httpStatus)
}



/** Provides a thin wrapper around the apache http utils package.
 *  You will probably want to set the maxTotalConnection, and defaultMaxConnectionPerRoute values for production.
 */
object RestService {
	val schemeRegistry = new SchemeRegistry()

	schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory, 80));
	schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory, 443));

	val clientConnectionManager = new ThreadSafeClientConnManager(schemeRegistry)

	clientConnectionManager.setMaxTotalConnections(200)
	clientConnectionManager.setDefaultMaxPerRoute(20)

	def addScheme(protocol: String, port: Int) {
		schemeRegistry.register(new Scheme(protocol, PlainSocketFactory.getSocketFactory(), port))
	}

	def maxTotalConnections(num: Int) = clientConnectionManager.setMaxTotalConnections(num);

	def maxConnectionsPerRoute(num: Int) = clientConnectionManager.setDefaultMaxPerRoute(num);
	
}

	}
}
