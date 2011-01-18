package scala_mash.shopify_api.model

import scala_mash.shopify_api.Utils._
import xml.NodeSeq
import org.joda.time.{LocalDate, DateTime}
import java.net.MalformedURLException

import bizondemand.utils.models.internet.Url
import scala_mash.shopify_api._
import scala_mash.rest.{Created,Ok,RestException}
import scala_mash.rest.util.Helpers.{ optionalYmd, optionalDateTimeWithTimeZone, optionalLong, optionalString, parseDateTimeWithTimeZone}

/**
 *
 * @author jimbarrows
 * @created: Dec 14, 2009 8:05:32 PM
 * @version 1.0
 *
 */

case class RecurringApplicationCharge(price: BigDecimal,
									  name: String,
									  returnUrl: Url,
									  test: Boolean,
									  billingOn: Option[LocalDate],
									  createdAt: Option[DateTime],
									  id: Option[Long],
									  status: Option[String],
									  updatedAt: Option[DateTime],
									  confirmationUrl: Option[Url]) {


  def toXml = <recurring-application-charge>
	<price type="float">{price.toString}</price>
	<name>{name}</name>
	<return-url>{returnUrl.toString}</return-url>
	<test type="boolean">{test}</test>
  </recurring-application-charge>

}

object RecurringApplicationCharge extends ShopifyResource[RecurringApplicationCharge] {

  def apply(price: BigDecimal, name: String, returnUrl: Url, test: Boolean) :RecurringApplicationCharge=
		  RecurringApplicationCharge(price,
			name,
			returnUrl,
			test,
			None,
			None,
			None,
			None,
			None,
			None)
  
	val url = shopifyUrl +/ ("admin")

	/**
	  * @param returnUrl - http://yourapp.com/charges/confirm
		*/
 	def create(	shop: ShopCredentials, 
				price: BigDecimal, 
 				name: String, 
				returnUrl: Url): RecurringApplicationCharge = {
		post(url +< shop.name +/ ("recurring_application_charges.xml"),
			Some(ShopifyPartnerInfo.apiKey),
			Some(ShopifyPartnerInfo.createPasswordForStore(shop.authenticationToken)),
			RecurringApplicationCharge(price, name, returnUrl, testOnly).toXml
		) match {
			case n:Created => parse(convertResponseToXml(n.response))
			case n => throw new RestException(n)
		}
	}

	def list (shop:ShopCredentials) : List[RecurringApplicationCharge] =  {
		get( url +< shop.name +/ ("recurring_application_charges.xml"),
			Some(ShopifyPartnerInfo.apiKey),
			Some(ShopifyPartnerInfo.createPasswordForStore(shop.authenticationToken))
		) match {
			case n:Ok => parseList(convertResponseToXml( n.response))
			case n => throw new RestException(n)
  		}
  	}

	def cancel (shop:ShopCredentials, id : Long)  =  {
		delete( url +< shop.name +/ ("recurring_application_charges") +/ (id.toString + ".xml"),
			Some(ShopifyPartnerInfo.apiKey),
			Some(ShopifyPartnerInfo.createPasswordForStore(shop.authenticationToken))
		)
	}

	def parseList(node:NodeSeq) : List[RecurringApplicationCharge] =  (node \\ "recurring-application-charge").map(parse(_)).toList

	def parse(node: NodeSeq): RecurringApplicationCharge = {
  		debug("Parsing: {} ", node.toString)
  		RecurringApplicationCharge(
			BigDecimal(node \ "price" text),
			node \ "name" text,
			{ try {Url(node \ "return-url" text)} catch { case mfue:MalformedURLException=>Url("http://localhost")}},
			testOnly,
			optionalYmd(node, "billing-on"),
			optionalDateTimeWithTimeZone(node, "created-at"),
			optionalLong(node, "id"),
			optionalString(node, "status" ),
			optionalDateTimeWithTimeZone(node, "updated-at"),
			{ try {Some(Url(node \ "confirmation-url" text))} catch { case mfue:MalformedURLException=>Some(Url("http://localhost"))}}
		)
	}
}
