package scala_mash.freshbooks_api

import xml.{XML, NodeSeq}
import bizondemand.utils.models.internet.{DomainName,Url, Http}
import scala_mash.rest.{RestService, HttpStatusCode}



case class Account( domainName:String, authenticationToken:String)


trait FreshbooksResource[T] extends RestService {
	val url = new Url(Http(), None, None, DomainName("freshbooks" :: "com" :: Nil), None, Some("api"::"2.1"::"xml-in"::Nil), None)
	
	/** override the original because password is always "x"
	*/	
	def post( domainName:String, authenticationToken:String, request :NodeSeq):HttpStatusCode = post( url +< domainName, Some(authenticationToken), Some("x"), request)
}
