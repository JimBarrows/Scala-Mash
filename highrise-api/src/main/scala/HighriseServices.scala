package scala_mash.highrise_api

import bizondemand.utils.models.internet.{Url, Https, DomainName}

import scala_mash.rest.{HttpStatusCode, Ok, Found, RestException}

import  xml.NodeSeq

import scala_mash.rest.RestService

/**
 *
 * @author jimbarrows
 * @created: Dec 31, 2009 10:29:02 AM
 * @version 1.0
 *
 */

case class Account(siteName:String, apiKey:String)

trait HighriseServices[T] extends RestService{

	val highrisehqCom = DomainName("highrisehq"  :: "com" ::Nil)
	
	val url = new Url(Https(), None, None, highrisehqCom, None, None, None)
  
	def siteName: String = System.getProperty("com.nsfw.highrisehq.siteName", "some_account")

	def apiKey: String = System.getProperty("com.nsfw.highrisehq.apiKey", "some_apiKey")

	def account = Account(siteName, apiKey)
  
	protected def getByUrl( url:Url, account:Account, parse: NodeSeq => T) : T = {
		debug( "HighriseServices[T].getByUrl( {}, {})", url, account)
		get( url,
			Some(account.apiKey),
			Some("x") 
		) match {
			case n:Ok => parse( convertResponseToXml(n.response))
			case n => throw new RestException(n)
		}
			
	}
	
	protected def defaultStatusHandler(httpStatus:HttpStatusCode) = {
		debug( "HighriseServices[T].defaultStatusHandler( {})", httpStatus )
		httpStatus match {
			//An error was thrown, and if it ends in login, it's probably the sitename, but without going back to the URL provided, we don't know.
			case n:Found if n.url.path == Some("login" :: Nil) => throw new LoginFailed()			
			case n => throw new RestException(n)
		} 
	}
}

class LoginFailed extends Exception {}



