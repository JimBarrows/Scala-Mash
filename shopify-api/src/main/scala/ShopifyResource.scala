package scala_mash.shopify_api

import bizondemand.utils.models.internet.{DomainName,Url, Http}
import scala_mash.rest.{HttpStatusCode, RestException}
import scala_mash.rest.RestService

/**
 *
 * @author jimbarrows
 * @created: Feb 4, 2010 11:15:32 PM
 * @version 1.0
 *
 */

trait ShopifyResource[T] extends RestService {
  val shopifyUrl = new Url(Http(), None, None, DomainName("myshopify" :: "com" :: Nil), None, None, None)

  def testOnly: Boolean = System.getProperty("com.nsfw.rest.shopify_resource.test", "true").toBoolean

  
	protected def defaultStatusHandler(httpStatus:HttpStatusCode) = {
		httpStatus match {
			case n => throw new RestException(n)
		} 
	}

}
