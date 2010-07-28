package bizondemand.freshbooks_api

import _root_.com.bizondemand.rest.{URL, MetaRestResource}

import xml._

case class Account( domainName:String, authenticationToken:String)

trait FreshbooksResource[T] extends MetaRestResource[T] {
	val url = new URL( "https", None, None, "freshbooks.com", None, Some("api"::"2.1"::"xml-in"::Nil), None)
	
	def post( domainName:String, authenticationToken:String, request :NodeSeq, parser:NodeSeq=>List[T]):List[T] = post( url +< domainName, authenticationToken, "x", request, parser)

	def post( domainName:String, authenticationToken:String, request :NodeSeq, parser:NodeSeq=>T):T = post( url +< domainName, authenticationToken, "x", request, parser)
}
