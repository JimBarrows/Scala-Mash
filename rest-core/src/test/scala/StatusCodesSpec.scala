package specs

import org.specs.Specification

import org.apache.http._
import org.apache.http.message._

import scala_mash.rest._

object HttpStatusCodeSpec extends Specification {

	"The HttpStatusCode class" should {
		"return a VariantAlsoNegotiates for a 506 status code" in {
			HttpStatusCode( createHttpResponse( 506, "506")) must haveClass[VariantAlsoNegotiates]
		}
		"return a InsufficientStorage for a 507 status code" in {
			HttpStatusCode( createHttpResponse( 507, "507")) must haveClass[InsufficientStorage]
		}
		"return a OtherServerError for a 508 status code" in {
			HttpStatusCode( createHttpResponse( 508, "508")) must haveClass[OtherServerError]
		}
		"return a BandwidthLimitExceeded for a 509 status code" in {
			HttpStatusCode( createHttpResponse( 509, "509")) must haveClass[BandwidthLimitExceeded]
		}
		"return a NotExtended for a 510 status code" in {
			HttpStatusCode( createHttpResponse( 510, "510")) must haveClass[NotExtended]
		}
		"return a OtherServerError for any status code between 511 and 599" in {
			HttpStatusCode( createHttpResponse( 511, "511")) must haveClass[OtherServerError]
			HttpStatusCode( createHttpResponse( 599, "599")) must haveClass[OtherServerError]
		}
	}
	def createHttpResponse( code: Int, reason: String) = {
		val version = new ProtocolVersion( "http", 1, 1)
		val statusLine = new BasicStatusLine( version, code, reason)
		new BasicHttpResponse( statusLine)
	}

}
