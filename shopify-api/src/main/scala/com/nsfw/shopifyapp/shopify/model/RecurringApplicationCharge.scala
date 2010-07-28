package com.nsfw.shopifyapp.shopify.model

import com.nsfw.shopifyapp.shopify.Utils._
import xml.NodeSeq
import org.joda.time.{LocalDate, DateTime}
import java.net.MalformedURLException
import com.nsfw.shopifyapp.shopify._

import bizondemand.utils.models.internet.Url
import bizondemand.rest.{Created,Ok,RestException}

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
			parseOptionalYearMonthDay(node \ "billing-on" text),
			Some(parseDateTimeWithTimeZone(node \ "created-at" text)),
			Some((node \ "id" text).toLong),
			Some(node \ "status" text),
			Some(parseDateTimeWithTimeZone(node \ "updated-at" text)),
			{ try {Some(Url(node \ "confirmation-url" text))} catch { case mfue:MalformedURLException=>Some(Url("http://localhost"))}}
		)
	}
}
