package bizondemand.shopify_api.model

import org.joda.time.DateTime
import xml.{Node, NodeSeq}

import bizondemand.shopify_api.Utils._
import bizondemand.shopify_api.{ShopifyPartnerInfo, ShopifyResource}
import bizondemand.rest.Ok
import bizondemand.utils.models.internet.{Url,Parameter}
import com.bizondemand.util.Helpers._


/**
 *
 * @author jimbarrows
 * @created: Dec 19, 2009 10:06:26 AM
 * @version 1.0
 *
 */
case class Address(val address1: String,
                   val address2: String,
                   val city: String,
                   val company: Option[String],
                   val country: String,
                   val firstName: String,
                   val lastName: String,
                   val phoneNumber: String,
                   val province: String,
                   val zip: String,
                   val name: String,
                   val countryCode: String,
                   val provinceCode: String) {
  def toXml = {
    <div class="adr">
      <div class="street-address">
        {address1}
      </div>
      <div class="extended-address">
        {address2}
      </div>
      <div class="locality">
        {city}
      </div>
      <div class="region">
        {province}
      </div>
      <div class="postal-code">
        {zip}
      </div>
      <div class="country-name">
        {country}
      </div>
    </div>
  }
}

object Address {
	def parse(node: NodeSeq) = {
		debug("Address.parse( {} )", node.toString)
		Address(
			node \ "address1" text,
			node \ "address2" text,
			node \ "city" text,
			optionalString(node, "company"),
			node \ "country" text,
			node \ "first-name" text,
			node \ "last-name" text,
			node \ "phone" text,
			node \ "province" text,
			node \ "zip" text,
			node \ "name" text,
			node \ "country-code" text,
			node \ "province-code" text
    	)
	}
}

case class LineItem(val fulfillmentService: String,
                    val fulfillmentStatus: Option[String],
                    val grams: Int,
                    val id: Int,
                    val price: BigDecimal,
                    val productId: Option[Int],
                    val quantity: Int,
                    val requiresShipping: Boolean,
                    val sku: String,
                    val title: String,
                    val variantId: Option[Int],
                    val variantTitle: Option[String],
                    val vendor: Option[String],
                    val name: String) {
    def toXml() = <line-item>
    	<fulfillment-service>{fulfillmentService}</fulfillment-service>
    	<fulfillment-status>{fulfillmentStatus}</fulfillment-status>
    	<grams>{grams}</grams>
    	<id>{id}</id>
    	<price>{price}</price>
    	<product>{productId}</product>
    	<quantity>{quantity}</quantity>
    	<requires-shipping>{requiresShipping}</requires-shipping>
    	<sku>{sku}</sku>
    	<title>{title}</title>
    	<variant-id>{variantId}</variant-id>
    	<vendor>{vendor}</vendor>
    	<name>{name}</name>
    </line-item>
}

object LineItem {
 	def parse(node: NodeSeq) = {
 		debug("LineItem.parse( {} )", node.toString)
    	LineItem(
    		node \ "fulfillment-service" text,
    		optionalString(node, "fulfillment-status"),
    		(node \ "grams" text).toInt,
    		(node \ "id" text).toInt,
    		BigDecimal(node \ "price" text),
    		optionalInt(node, "product-id"),
    		(node \ "quantity" text).toInt,
    		(node \ "requires-shipping" text).toBoolean,
    		node \ "sku" text,
    		node \ "title" text,
    		optionalInt(node,"variant-id"),
    		optionalString(node, "variant-title"),
    		optionalString(node, "vendor"),
    		node \ "name" text
    	)
	}	
	def parseList(node : NodeSeq) : List[LineItem] = {
		debug("LineItem.parseList( {} )", node.toString)
		(node \\ "line-items" \ "line-item").map( parse(_)).toList
	}
}


case class ShippingLine(val code: String,
                        val price: BigDecimal,
                        val title: String) {
}
object ShippingLine {
  
	def parse(node: NodeSeq) = ShippingLine(
    	node \ "code" text,
    	BigDecimal(node \ "price" text),
    	node \ "title" text
    )

	def parseList(node : NodeSeq) : List[ShippingLine] = (node \\ "shipping-lines" \ "shipping-line").map( parse(_)).toList
}


case class TaxLine(val price: BigDecimal,
                   val rate: BigDecimal,
                   val title: String) {
}
object TaxLine {
	def parse(node: NodeSeq) = Some(TaxLine(
		BigDecimal(node \ "price" text),
		BigDecimal(node \ "rate" text),
		node \ "title" text
    	))

}


//Not used yet
//class NoteAttribute() extends RestResource[NoteAttribute] {
//  def restParse = NoteAttribute
//}
//object NoteAttribute extends NoteAttribute with MetaRestResource[NoteAttribute] {
//}



case class PaymentDetail(	val creditCardNumber: String,
    						val creditCardCompany: String) {
}

object PaymentDetail {
	def parse(node: NodeSeq) = Some(PaymentDetail(
		node \ "credit-card-number" text,
		node \ "credit-card-company" text
	))
}

case class Order(val buyerAcceptsMarketing: Boolean,
      val closedAt: Option[DateTime],
      val createdAt: DateTime,
      val currency: String,
      val email: String,
      val id: Int,
      val name: String,
      val number: Int,
      val subtotalPrice: BigDecimal,
      val totalDiscounts: BigDecimal,
      val totalLineItemsPrice: BigDecimal,
      val totalPrice: BigDecimal,
      val totalTax: BigDecimal,
      val totalWeight: Int,
      val updatedAt: DateTime,
      val orderNumber: Int,
      val billingAddress: Address,
      val shippingAddress: Address,
      val lineItems: List[LineItem],
      val shippingLines: List[ShippingLine],
      val paymentDetail: PaymentDetail) {
}

object Order extends ShopifyResource[Order] {

  val url: Url = shopifyUrl +/ ("admin")

	def parse(node: NodeSeq) = {
		debug("Order.parse( {} )", node.toString)
		Order(
    		(node \ "buyer-accepts-marketing" text).toBoolean,
    		(if ((node \ "closed-at" text).isEmpty) None else Some(parseDateTimeWithTimeZone(node \ "closed-at" text))),
    		parseDateTimeWithTimeZone(node \ "created-at" text),
    		node \ "currency" text,
    		node \ "email" text,
    		(node \ "id" text).toInt,
    		node \ "name" text,
    		(node \ "number" text).toInt,
    		BigDecimal(node \ "subtotal-price" text),
    		BigDecimal(node \ "total-discounts" text),
    		BigDecimal(node \ "total-line-items-price" text),
    		BigDecimal(node \ "total-price" text),
    		BigDecimal(node \ "total-tax" text),
    		(node \ "total-weight" text).toInt,
    		parseDateTimeWithTimeZone(node \ "updated-at" text),
    		(node \ "order-number" text).toInt,
    		Address.parse(node \ "billing-address"),
    		Address.parse(node \ "shipping-address"),
    		LineItem.parseList(node),
    		ShippingLine.parseList( node),
    		PaymentDetail.parse(node \ "payment-details").get
    	)
	}

	def parseList(node: NodeSeq): List[Order] = (node \\ "order").map( parse(_)).toList

	def findAllOrders(shop: ShopCredentials): List[Order] = {
		get(url +< shop.name +/ "orders.xml", 
			Some(ShopifyPartnerInfo.apiKey), 
			Some(ShopifyPartnerInfo.createPasswordForStore(shop.authenticationToken))
		) match {
			case n:Ok => parseList(convertResponseToXml(n.response)).sort(_.orderNumber < _.orderNumber) 
		}
	}

	/**Find all orders that have been created or updated since.
	*
	*/
	def findAllOrdersCreatedOrUpdatedSince(shop: ShopCredentials, since: DateTime): List[Order] = {
		(findAllOrdersCreatedSince(shop, since) ::: findAllOrdersUpdatedSince(shop, since)).sort(_.orderNumber < _.orderNumber)
	}

	def findAllOrdersCreatedSince(shop: ShopCredentials, since: DateTime): List[Order] = {
		get(url +< shop.name +/ "orders.xml" +& Parameter("create_at_min", printWithTimeZone(since)),
    		Some(ShopifyPartnerInfo.apiKey),
    		Some(ShopifyPartnerInfo.createPasswordForStore(shop.authenticationToken))
    	) match {
    		case n:Ok => parseList(convertResponseToXml(n.response)).sort(_.orderNumber < _.orderNumber)
		}
	}

	def findAllOrdersUpdatedSince(shop: ShopCredentials, since: DateTime): List[Order] = {

		get(url +< shop.name +/ "orders.xml" +& Parameter("updated_at_min", printWithTimeZone(since)),
			Some(ShopifyPartnerInfo.apiKey),
			Some(ShopifyPartnerInfo.createPasswordForStore(shop.authenticationToken))
		) match {
			case n:Ok => parseList(convertResponseToXml(n.response)).sort(_.orderNumber < _.orderNumber)
		}
	}
}
