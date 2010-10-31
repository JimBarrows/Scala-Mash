package scala_mash {
	package rest {

import org.apache.http.HttpResponse
import io.Source

import bizondemand.utils.models.internet.Url

import bizondemand.utils.logging.Log

class HttpStatusCode( code:Int, description:String)

object HttpStatusCode extends Log {
	
	def apply( response : HttpResponse):HttpStatusCode = {
		
		debug("HttpStatusCode.apply response: {}", response)
		response.getStatusLine().getStatusCode match {
			case 100 => Continue()
			case 101 => SwitchingProtocols()
			case n if( (n>101) && (n<200)) => OtherInformational(n,response.getStatusLine.getReasonPhrase)
			case 200 => Ok( Source.fromInputStream(response.getEntity.getContent).mkString)
			case 201 => Created( Source.fromInputStream(response.getEntity.getContent).mkString, 
					Url(response.getFirstHeader("Location").getValue()))
			case 202 => Accepted ()
			case 203 =>NonAuthoritativeInformation( Source.fromInputStream(response.getEntity.getContent).mkString)
			case 204 => NoContent()
			case 205 => ResetContent()
			case 206 => PartialContent()
			case n if( n>206 && n <300) => OtherSuccess(n,response.getStatusLine.getReasonPhrase)
			case 300 => MultipleChoices()
			case 301 => MovedPermanently(Url(response.getFirstHeader("Location").getValue()))
			case 302 => Found(Url(response.getFirstHeader("Location").getValue()))
			case 303 => SeeOther(Url(response.getFirstHeader("Location").getValue()))
			case 304 => NotModified()
			case 305 => UseProxy(Url(response.getFirstHeader("Location").getValue()))
			case 306 => Unused()
			case 307 => TemporaryRedirect(Url(response.getFirstHeader("Location").getValue()))
			case n if( n>307 && n <400) => OtherSuccess(n,response.getStatusLine.getReasonPhrase)
			case 400 => BadRequest()
			case 401 => Unauthorized()
			case 402 => PaymentRequired()
			case 403 => Forbidden()
			case 404 => NotFound()
			case 405 => MethodNotAllowed()
			case 406 => NotAcceptable()
			case 407 => ProxyAuthenticationRequired()
			case 408 => RequestTimeout()
			case 409 => Conflict()
			case 410 => Gone()
			case 411 => LengthRequired()
			case 412 => PreconditionFailed()
			case 413 => RequestEntityTooLarge()
			case 414 => RequestUriTooLong()
			case 415 => UnsupportedMediaType()
			case 416 => RequestedRangeNotSatisfiable()
			case 417 => ExpectationFailed()
			case n if( n > 418 && n < 500) => OtherClientError(n,response.getStatusLine.getReasonPhrase)
			case 500 => InternalServerError()
			case 501 => NotImplemented()
			case 502 => BadGateway()
			case 503 => ServiceUnavailable()
			case 504 => GatewayTimeout()
			case 505 => HttpVersionNotSupported()			
			case 506 => VariantAlsoNegotiates()			
			case 507 => InsufficientStorage()			
			case 509 => BandwidthLimitExceeded()			
			case 510 => NotExtended()			
			case n if ((n==508) || ( n > 510 && n< 600)) => OtherServerError(n, response.getStatusLine.getReasonPhrase)
			case n => new HttpStatusCode( n, response.getStatusLine.getReasonPhrase)
		}
	}

}

class Informational ( code:Int, description:String) extends HttpStatusCode( code, description) {
	
	if( code < 100 || code > 199 ) throw new IllegalArgumentException("Informational codes must be 100 - 199 you provided %d".format(code))
}

class Successful( code:Int, description:String) extends HttpStatusCode( code, description) {
	
	if( code < 200 || code > 299 ) throw new IllegalArgumentException("Successful codes must be 200 - 299 you provided %d".format(code))
}

class Redirection( code:Int, description:String) extends HttpStatusCode( code, description) {
	
	if( code < 300 || code > 399 ) throw new IllegalArgumentException("Redirection codes must be 300 - 399 you provided %d".format(code))
}

class ClientError( code:Int, description:String) extends HttpStatusCode( code, description) {
	
	if( code < 400 || code > 499 ) throw new IllegalArgumentException("Client Error codes must be 400 - 499 you provided %d".format(code))
}

class ServerError( code:Int, description:String) extends HttpStatusCode( code, description) {
	
	if( code < 500 || code > 599 ) throw new IllegalArgumentException("Server Error codes must be 500 - 599 you provided %d".format(code))
}


case class Continue() extends Informational(100,"Continue"){}

case class SwitchingProtocols() extends Informational(101,"Switching Protocols"){}

case class OtherInformational(code:Int, description:String) extends Informational ( code, description){}

case class Ok(response:String) extends Successful(200, "Success"){}

case class Created(response : String, location : Url) extends Successful(201, "Success"){}

case class Accepted() extends Successful(202, "Accepted"){}

case class NonAuthoritativeInformation(response:String) extends Successful(203, "Non-Authoritative Information"){}

case class NoContent() extends Successful(204, "No Content"){}

case class ResetContent() extends Successful(205, "Reset Content"){}

case class PartialContent() extends Successful(206, "Partial Content"){}

case class OtherSuccess( code:Int, description:String) extends Successful ( code, description){}

case class MultipleChoices() extends Redirection(300, "Multiple Choices") {}

case class MovedPermanently(newLocation:Url) extends Redirection(301, "Moved Permanently"){}

case class Found(location:Url) extends Redirection(302,"Found"){
	def url = location
}

case class SeeOther(location:Url) extends Redirection(303,"See Other"){}

case class NotModified() extends Redirection(304, "Not modifified") {}

case class UseProxy(proxy:Url) extends Redirection(305, "Use Proxy"){}

case class Unused() extends Redirection(306, "Unused"){}

case class TemporaryRedirect(url:Url) extends Redirection(307,"Temporary Redirect"){}

case class OtherRedirection(code:Int, description:String) extends Redirection ( code, description){}

case class BadRequest() extends ClientError(400, "Bad Request"){}

case class Unauthorized() extends ClientError(401, "Unauthorized"){}

case class PaymentRequired() extends ClientError(402,"Payment Required") {}

case class Forbidden() extends ClientError(403,"Forbidden"){}

case class NotFound() extends ClientError(404,"Not Found"){}

case class MethodNotAllowed() extends ClientError(405,"Method Not Allowed") {}

case class NotAcceptable() extends ClientError(406, "NotAcceptable"){}

case class ProxyAuthenticationRequired() extends ClientError(407, "Proxy Authentication Required"){}

case class RequestTimeout() extends ClientError(408, "Request Timeout") {}

case class Conflict() extends ClientError(409,"Conflict"){}

case class Gone() extends ClientError(410, "Gone") {}

case class LengthRequired() extends ClientError(411, "Length Required"){}

case class PreconditionFailed() extends ClientError(412, "Precondition Failed"){}

case class RequestEntityTooLarge() extends ClientError(413, "Request Entity Too Large") {}

case class RequestUriTooLong() extends ClientError(414, "Request-URI Too Long"){}

case class UnsupportedMediaType() extends ClientError(415, "Unsupported Media Type"){}

case class RequestedRangeNotSatisfiable() extends ClientError(416, "Requested Range Not Satisfiable"){}

case class ExpectationFailed() extends ClientError(417, "Expectation Failed"){}

case class OtherClientError(code:Int, description:String) extends ClientError(code, description){}

case class InternalServerError() extends ServerError( 500, "Internal Server Error"){}

case class NotImplemented() extends ServerError(501, "Not Implemented"){}

case class BadGateway() extends ServerError(502, "Bad Gateway"){}

case class ServiceUnavailable() extends ServerError(503,"Service Unavailable"){}

case class GatewayTimeout() extends ServerError(504, "Gateway Timeout"){}

case class HttpVersionNotSupported() extends ServerError(505, "HTTP Version Not Supported"){}

case class VariantAlsoNegotiates() extends ServerError(506, "Variant Also Negotiates (RFC 2295)"){}

case class InsufficientStorage() extends ServerError(507, "Insufficient Storage (WebDAV) (RFC 4918)"){}

case class BandwidthLimitExceeded() extends ServerError(509, "Bandwidth Limit Exceeded (Apache bw/limited extension)"){}

case class NotExtended() extends ServerError(510, "Not Extended (RFC 2774)"){}


case class OtherServerError(code:Int, description:String) extends ServerError(code, description){}

	}
}
