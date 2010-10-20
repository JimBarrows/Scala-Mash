package scala_mash.shopify_api.model

import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormatterBuilder}
import xml.{NodeSeq, Node}

import scala_mash.shopify_api.{ShopifyPartnerInfo, ShopifyResource}
import bizondemand.utils.models.internet.Url
import scala_mash.rest._
import util.Helpers._

/**
 *
 * @author jimbarrows
 * @created: Dec 14, 2009 7:56:27 PM
 * @version 1.0
 *
 */
case class ApplicationCharge( name: String,
							price: BigDecimal,
							returnUrl: Url,
							createdAt: Option[DateTime],
							id: Option[Int],
							status: Option[String],
							test: Boolean,
							updatedAt: Option[DateTime],
							confirmationUrl: Option[Url]) {
	def toXml = <application-charge>
			<price type="float">{price.toString}</price>
			<name>{name}</name>
			<return-url>{returnUrl}</return-url>
		</application-charge>

	def activate = ApplicationCharge( name, price,returnUrl, createdAt, id, Some("Active"), test, updatedAt, confirmationUrl)
}



object ApplicationCharge extends ShopifyResource[ApplicationCharge] {

	val url = shopifyUrl +/ ("admin") +/ ("application_charges.xml")

	/**Creates a single charge to the application user.  Upon return the confirmationUrl will be set, and the 
		* application <em>must</em> redirect to it.  Then shopify will redirect them to the returnUrl you provide, at 
		* that time you have to confirm that the charge is correct etc.  See http://api.shopify.com/billing.html for 
		* details.
	  */
 	def charge(shop: ShopCredentials, 
 			name: String, 
 			price: BigDecimal, 
 			returnUrl: Url, 
 			test: Boolean): ApplicationCharge = {

 		debug("ApplicationCharge.charge( {}, {},{},{},{})", shop, name, price,returnUrl, test)

		val applicationCharge = ApplicationCharge(  name, 
				price, 
				returnUrl, 
				None, 
				None, 
				None, 
				test, 
				None,
				None)
		val status = post(url +< shop.name, 
				Some(ShopifyPartnerInfo.apiKey),
				Some(ShopifyPartnerInfo.createPasswordForStore(shop.authenticationToken)),
				applicationCharge.toXml
			)

		debug("ApplicationCharge.charge - returned: {}", status)

		status match {
			case n:Created=> parse( convertResponseToXml(n.response))
			case n => throw new RestException(n)
		}
	}

	def confirm( chargeId:Long) = {
		//	find(chargeId).activate;
	}

	def findAll(shop:ShopCredentials) = {
		get( url +< shop.name, 
			Some(ShopifyPartnerInfo.apiKey), 
			Some(ShopifyPartnerInfo.createPasswordForStore(shop.authenticationToken))
		) match {
			case n:Ok => parseList( convertResponseToXml(n.response))
			case n => throw new RestException(n)
		}
	}

	def parseList(node:NodeSeq):List[ApplicationCharge] = (node\\"records").map(parse(_)).toList
  
	def parse(node: NodeSeq): ApplicationCharge = {
		debug("ApplicationCharge.parse( {})", node )
		ApplicationCharge(
				node \ "name" text,
				BigDecimal((node \ "price" text)),
				Url(node \ "return-url" text),
    		Some(parseDateTimeWithTimeZone(node \ "created-at" text)),
    		optionalInt( node, "id"), 
    		optionalString(node, "status"),
    		boolean(node, "test"),
    		optionalDateTimeWithTimeZone( node, "updated-at"),
    		Some(Url(node \ "confirmation-url" text))
    	)
	}
}
