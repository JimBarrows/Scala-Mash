package scala_mash.shopify_api.model

import org.joda.time.DateTime
import xml.NodeSeq
import bizondemand.utils.logging.Log
import bizondemand.utils.models.internet.{DomainName,Url}
import scala_mash.rest.Ok
import scala_mash.shopify_api.{ShopifyPartnerInfo, ShopifyResource}
import scala_mash.shopify_api.Utils._
import scala_mash.rest.util.Helpers.{parseDateTimeWithTimeZone,optionalYmd, optionalBoolean, optionalLong, optionalString}

/**
 *
 * @author jimbarrows
 * @created: Dec 11, 2009 7:46:03 AM
 * @version 1.0
 *
 */

case class Shop(activeSubscriptionId: Option[Long],
                address1: String,
                city: String,
                country: String,
                createdAt: DateTime,
                domain: String,
                email: String,
                id: Long,
                name: String,
                phone: String,
                province: String,
                public: Boolean,
                source: Option[String],
                zip: String,
                currency: String,
                timezone: String,
                shopOwner: String,
                moneyFormat: String,
                moneyWithCurrencyFormat: String,
                taxesIncluded: Option[Boolean],
                taxShipping: Option[Boolean],
                planName:String) {
}

case class ShopCredentials(name: String, signature: String, authenticationToken: String) {
}

object Shop extends ShopifyResource[Shop] with Log{
 	val url: Url = shopifyUrl +/ "admin" +/ "shop.xml"

 	def parseShop(node: NodeSeq): Shop = {
		Shop(optionalLong( node, "active-subscription-id"),
			node \ "address1" text,
			node \ "city" text,
			node \ "country" text,
			parseDateTimeWithTimeZone(node \ "created-at" text),
			node \ "domain" text,
			node \ "email" text,
			(node \ "id" text).toLong,
			node \ "name" text,
			node \ "phone" text,
			node \ "province" text,
			(node \ "public" text).toBoolean,
			optionalString( node, "source"),
			node \ "zip" text,
			node \ "currency" text,
			node \ "timezone" text,
			node \ "shop-owner" text,
			node \ "money-format" text,
			node \ "money-with-currency-format" text,
			optionalBoolean(node, "taxes-included" ),
			optionalBoolean( node, "tax-shipping" ),
			node \ "plan-name" text
		)
	}


  def findShop(shop: ShopCredentials): Shop = {
		get(url +< shop.name, Some(ShopifyPartnerInfo.apiKey), Some(ShopifyPartnerInfo.createPasswordForStore(shop.authenticationToken))) match {
    	case n:Ok => parseShop( convertResponseToXml(n.response))
			case n => defaultStatusHandler(n)
    }
  }
}
 
