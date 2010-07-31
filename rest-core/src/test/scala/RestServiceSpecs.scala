package test

import org.specs.Specification
import org.specs.mock.Mockito
import org.mockito.Matchers._
import org.mockito.Mockito._

import scala_mash.rest.{ RestService, Ok,Found}

import org.apache.http.HttpVersion
import org.apache.http.message.{BasicStatusLine, BasicHttpResponse}

import org.apache.http.auth.{UsernamePasswordCredentials, AuthScope}
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.HttpResponse
import org.apache.http.entity.{StringEntity}
import org.apache.http.conn.scheme.{PlainSocketFactory, Scheme, SchemeRegistry}
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager
import org.apache.http.client.methods._

import bizondemand.utils.models.internet.{Url,Http, DomainName}



class RestServiceSpecs extends Specification with Mockito {			
	
	"The RestServices" should {
		"return a 200 and it's entity from a succesful post" in {
			
			// println("Setting request up")
			// 			val expectedRequest = <begin> This is the Request </begin>
			// 			val expectedRequestUrl = "http://localhost/dummy/url"
			// 			val postMethod = new HttpPost(expectedRequestUrl)
			// 			postMethod.setEntity(new StringEntity(expectedRequest.toString))
			// 						    		
			// 			println("Setting response up")
			// 			val expectedResponseEntity = "This is the expected entity"
			// 			val expectedResponse : HttpResponse = new BasicHttpResponse(new BasicStatusLine( HttpVersion.HTTP_1_1, 200, "Success"))
			// 			expectedResponse.setEntity( new StringEntity(expectedResponseEntity))
			// 									
			// 			val httpClient = mock [DefaultHttpClient]
			// 									
			// 			httpClient.execute(postMethod) returns expectedResponse			
			// 									
			// 			class TestService extends MetaRestResource {	
			// 				override def createConnectionManager = { 
			// 					println("Returning fake httpclient")
			// 					httpClient	
			// 				}
			// 			}	
			// 							
			// 			val testService = new TestService()
			// 									
			// 			println("Go")
			// 			testService.post(URL(expectedRequestUrl), None, None, expectedRequest ) must be_== ( Ok(expectedResponseEntity))
			// 			httpClient.execute(postMethod) was called
		 }
		 
		"Bug 266 - Status code is returning a null" in {
		 	val url = Url( Http(), DomainName("TestAccountForMe"::"highrisehq"::"com"::Nil), "people.xml"::Nil)
		 	val xml= <person>
        		<id type="integer"></id>
        		<first-name>Joe</first-name>
        		<last-name>Tester</last-name>
        		<title>JoeTester Title</title>
        		<background>Background</background>
        		<company-id type="integer"></company-id>
        		<created-at type="datetime"> </created-at>
                <updated-at type="datetime"> </updated-at>
        		<visible-to>Everyone</visible-to>
        		<owner-id type="integer"></owner-id>
        		<group-id type="integer"></group-id>
        		<author-id type="integer"></author-id>
                <contact-data>
    			</contact-data>
    		</person>
    		
    		object TestService extends RestService{}
    		
    		val statusCode = TestService.post( url, Some("1ad2fc1adf9e7fc1f342d0e431069af0"), Some("x"), xml)
    		
    		statusCode must notBeNull
		}
		
		"Should handle a 302 by returnng a Found class" in {
		 	val url = Url( Http(), DomainName("TestAccountForMe"::"highrisehq"::"com"::Nil), "people.xml"::Nil)
		 	val xml= <person>
        		<id type="integer"></id>
        		<first-name>Joe</first-name>
        		<last-name>Tester</last-name>
        		<title>JoeTester Title</title>
        		<background>Background</background>
        		<company-id type="integer"></company-id>
        		<created-at type="datetime"> </created-at>
                <updated-at type="datetime"> </updated-at>
        		<visible-to>Everyone</visible-to>
        		<owner-id type="integer"></owner-id>
        		<group-id type="integer"></group-id>
        		<author-id type="integer"></author-id>
                <contact-data>
    			</contact-data>
    		</person>
    		
    		object TestService extends RestService{}
    		
    		val statusCode = TestService.post( url, Some("1ad2fc1adf9e7fc1f342d0e431069af0"), Some("x"), xml)
    		
    		statusCode must be_== (Found( Url("https://TestAccountForMe.highrisehq.com/people.xml")))
		}
	}
}