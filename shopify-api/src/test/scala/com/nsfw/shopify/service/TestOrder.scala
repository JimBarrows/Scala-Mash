package com.nsfw.shopify.service

import org.specs.Specification
import org.specs.runner.JUnit4
import com.nsfw.shopifyapp.shopify.model._
import com.nsfw.shopifyapp.shopify.Utils._

/**
 *
 * @author jimbarrows
 * @created: Dec 19, 2009 10:57:06 AM
 * @version 1.0
 *
 */

object AddressSpec extends Specification {
	"The address class" should {
		"be able to parse a response with no company" in {
    		val xmlToParse = <billing-address>
    			<address1>Chestnut Street 92</address1>
    			<address2></address2>
				<city>Louisville</city>
				<company nil="true"></company>
				<country>United States</country>
				<first-name>Bob</first-name>
				<last-name>Norman</last-name>
        		<phone>555-625-1199</phone>
        		<province>Kentucky</province>
        		<zip>40202</zip>
        		<name>Bob Norman</name>
        		<country-code>US</country-code>
        		<province-code>KY</province-code>
    		</billing-address>

    		val address = Address("Chestnut Street 92", "", "Louisville", None, "United States", "Bob", "Norman", "555-625-1199", "Kentucky", "40202", 
    			"Bob Norman", "US", "KY")
     		val response = Address.parse(xmlToParse)
     		response  must be_==(address)
    	}
    	"be able to parse a response with a company" in {
     		val xmlToParse = <billing-address>
    			<address1>Chestnut Street 92</address1>
        		<address2></address2>
        		<city>Louisville</city>
        		<company>Some Company</company>
        		<country>United States</country>
        		<first-name>Bob</first-name>
        		<last-name>Norman</last-name>
        		<phone>555-625-1199</phone>
        		<province>Kentucky</province>
        		<zip>40202</zip>
        		<name>Bob Norman</name>
        		<country-code>US</country-code>
        		<province-code>KY</province-code>
      		</billing-address>

     		val address = Address("Chestnut Street 92", "", "Louisville", Some("Some Company"), 
     			"United States", "Bob", "Norman", "555-625-1199", "Kentucky", "40202", "Bob Norman", "US", 
     			"KY")
    		val response = Address.parse(xmlToParse)
      		response must be_==(address)
    	}
	}
}

object LineItemSpec extends Specification {
	"The LineItem class" should {
    	"be able to parse a response with fulfillment status and vendar are nil" in {
    		val xmlToParse = <line-item>
        		<fulfillment-service>manual</fulfillment-service>
        		<fulfillment-status nil="true"></fulfillment-status>
        		<grams type="integer">200</grams>
        		<id type="integer">703073503</id>
        		<price type="decimal">199.00</price>
        		<product-id type="integer">1706652214</product-id>
        		<quantity type="integer">1</quantity>
        		<requires-shipping type="boolean">true</requires-shipping>
        		<sku>IPOD2008BLACK</sku>
        		<title>IPod Nano - 8gb</title>
        		<variant-id type="integer">457924702</variant-id>
        		<variant-title>black</variant-title>
        		<vendor nil="true"></vendor>
        		<name>IPod Nano - 8gb - black</name>
      		</line-item>
    		val expected = LineItem("manual", None, 200, 703073503, BigDecimal("199.00"), Some(1706652214), 1, true, "IPOD2008BLACK", "IPod Nano - 8gb", 
    				Some(457924702), Some("black"), None, "IPod Nano - 8gb - black")
    		val response = LineItem.parse(xmlToParse)
    		response must be_==(expected)
    	}
  	}

 	"be able to parse a response with fulfillment status and vendor are not nil" in {
    	val xmlToParse = <line-item>
    		<fulfillment-service>manual</fulfillment-service>
    		<fulfillment-status>Some Status</fulfillment-status>
    		<grams type="integer">200</grams>
    		<id type="integer">703073503</id>
    		<price type="decimal">199.00</price>
    		<product-id type="integer">1706652214</product-id>
    		<quantity type="integer">1</quantity>
    		<requires-shipping type="boolean">true</requires-shipping>
    		<sku>IPOD2008BLACK</sku>
    		<title>IPod Nano - 8gb</title>
    		<variant-id type="integer">457924702</variant-id>
    		<variant-title>black</variant-title>
      		<vendor>Vendor Name</vendor>
      		<name>IPod Nano - 8gb - black</name>
    	</line-item>
    	val expected = LineItem("manual", Some("Some Status"), 200, 703073503, BigDecimal("199.00"), Some(1706652214), 1, true, "IPOD2008BLACK", 
    		"IPod Nano - 8gb", Some(457924702), Some("black"), Some("Vendor Name"), "IPod Nano - 8gb - black")
    	val response = LineItem.parse(xmlToParse)
    	response must be_==(expected)
  	}
  	
  	"be able to parse a response with an empty product-id" in {
  		val xmlToParse = <line-item>
    		<fulfillment-service>manual</fulfillment-service>
    		<fulfillment-status>Some Status</fulfillment-status>
    		<grams type="integer">200</grams>
    		<id type="integer">703073503</id>
    		<price type="decimal">199.00</price>
    		<product-id type="integer"></product-id>
    		<quantity type="integer">1</quantity>
    		<requires-shipping type="boolean">true</requires-shipping>
    		<sku>IPOD2008BLACK</sku>
    		<title>IPod Nano - 8gb</title>
    		<variant-id type="integer">457924702</variant-id>
    		<variant-title>black</variant-title>
      		<vendor>Vendor Name</vendor>
      		<name>IPod Nano - 8gb - black</name>
    	</line-item>
    	val expected = LineItem("manual", Some("Some Status"), 200, 703073503, BigDecimal("199.00"), None, 1, true, "IPOD2008BLACK", 
    		"IPod Nano - 8gb", Some(457924702), Some("black"), Some("Vendor Name"), "IPod Nano - 8gb - black")
    	val response = LineItem.parse(xmlToParse)
    	response must be_==(expected)
  	}
  	
  	"be able to parse a response with variant id and title are nil" in {
    	val xmlToParse = <line-item>
    		<fulfillment-service>manual</fulfillment-service>
    		<fulfillment-status>Some Status</fulfillment-status>
    		<grams type="integer">200</grams>
    		<id type="integer">703073503</id>
    		<price type="decimal">199.00</price>
    		<product-id type="integer">1706652214</product-id>
    		<quantity type="integer">1</quantity>
    		<requires-shipping type="boolean">true</requires-shipping>
    		<sku>IPOD2008BLACK</sku>
    		<title>IPod Nano - 8gb</title>
    		<variant-id type="integer"></variant-id>
    		<variant-title></variant-title>
      		<vendor>Vendor Name</vendor>
      		<name>IPod Nano - 8gb - black</name>
    	</line-item>
    	val expected = LineItem("manual", Some("Some Status"), 200, 703073503, BigDecimal("199.00"), Some(1706652214), 1, true, "IPOD2008BLACK", 
    		"IPod Nano - 8gb", None, None, Some("Vendor Name"), "IPod Nano - 8gb - black")
    	val response = LineItem.parse(xmlToParse)
    	response must be_==(expected)
  	}
}

object ShippingLineSpec extends Specification {
	"The ShippingLine class" should {
    	"be able to parse a response" in {
      		val xmlToParse = <shipping-line>
        		<code>Free Shipping</code>
        		<price type="decimal">0.00</price>
        		<title>Free Shipping</title>
      		</shipping-line>

      		val expected = ShippingLine("Free Shipping", BigDecimal("0.00"), "Free Shipping")
      		val response = ShippingLine.parse(xmlToParse)
      		response must be_==(expected)
    	}
 	}
}

object TaxLineSpec extends Specification {
	"The TaxLine class" should {
    	"be able to parse a response" in {
      		val xmlToParse = <tax-line>
        		<price type="decimal">11.94</price>
        		<rate type="float">0.06</rate>
        		<title>State Tax</title>
      		</tax-line>
      		val expected = TaxLine(BigDecimal("11.94"), BigDecimal("0.06"), "State Tax")
      		val response = TaxLine.parse(xmlToParse)
      		response must beSome[TaxLine].which(_ must be_==(expected))
    	}
  	}
}

object PaymentDetailSpec extends Specification {
	"The PaymentDetail class" should {
    	"be able to parse a response" in {
      		val xmlToParse = <payment-details>
        		<credit-card-number>XXXX-XXXX-XXXX-4242</credit-card-number>
        		<credit-card-company>Visa</credit-card-company>
      		</payment-details>
      		val expected = PaymentDetail("XXXX-XXXX-XXXX-4242", "Visa")
      		val response = PaymentDetail.parse(xmlToParse)
      		response must beSome[PaymentDetail].which(_ must be_==(expected))
    	}
  	}
}


object OrderSpec extends Specification {
	"The order class" should {
    	"be able to parse a response" in {
      		val xmlToParse = <order>
        		<buyer-accepts-marketing type="boolean">false</buyer-accepts-marketing>
		        <closed-at type="datetime" nil="true"></closed-at>
        		<created-at type="datetime">2008-01-10T11:00:00-05:00</created-at>
        		<currency>USD</currency>
        		<email>bob.norman@hostmail.com</email>
        		<financial-status>authorized</financial-status>
        		<fulfillment-status nil="true"></fulfillment-status>
        		<gateway>authorized_net</gateway>
        		<id type="integer">1524531292</id>
        		<landing-site>http://www.example.com?source=abc</landing-site>
        		<name>#1001</name>
        		<note nil="true"></note>
        		<number type="integer">1</number>
        		<referring-site>http://www.otherexample.com</referring-site>
        		<subtotal-price type="decimal">199.00</subtotal-price>
        		<taxes-included type="boolean">false</taxes-included>
        		<token>b1946ac92492d2347c6235b4d2611184</token>
        		<total-discounts type="decimal">0.00</total-discounts>
        		<total-line-items-price type="decimal">199.00</total-line-items-price>
        		<total-price type="decimal">210.94</total-price>
        		<total-tax type="decimal">11.94</total-tax>
        		<total-weight type="integer">0</total-weight>
        		<updated-at type="datetime">2008-01-10T11:00:00-05:00</updated-at>
        		<browser-ip nil="true"></browser-ip>
        		<landing-site-ref>abc</landing-site-ref>
        		<order-number type="integer">1001</order-number>
        		<billing-address>
        			<address1>Chestnut Street 92</address1>
          			<address2></address2>
          			<city>Louisville</city>
          			<company nil="true"></company>
          			<country>United States</country>
          			<first-name>Bob</first-name>
          			<last-name>Norman</last-name>
          			<phone>555-625-1199</phone>
         			<province>Kentucky</province>
          			<zip>40202</zip>
          			<name>Bob Norman</name>
          			<country-code>US</country-code>
          			<province-code>KY</province-code>
        		</billing-address>
        		<shipping-address>
          			<address1>Chestnut Street 92</address1>
          			<address2></address2>
          			<city>Louisville</city>
          			<company nil="true"></company>
          			<country>United States</country>
          			<first-name>Bob</first-name>
          			<last-name>Norman</last-name>
          			<phone>555-625-1199</phone>
          			<province>Kentucky</province>
          			<zip>40202</zip>
          			<name>Bob Norman</name>
          			<country-code>US</country-code>
          			<province-code>KY</province-code>
        		</shipping-address>
        		<line-items type="array">
          			<line-item>
            			<fulfillment-service>manual</fulfillment-service>
            			<fulfillment-status nil="true"></fulfillment-status>
            			<grams type="integer">200</grams>
            			<id type="integer">703073503</id>
            			<price type="decimal">199.00</price>
            			<product-id type="integer">1706652214</product-id>
            			<quantity type="integer">1</quantity>
            			<requires-shipping type="boolean">true</requires-shipping>
            			<sku>IPOD2008BLACK</sku>
            			<title>IPod Nano - 8gb</title>
            			<variant-id type="integer">457924702</variant-id>
            			<variant-title>black</variant-title>
            			<vendor nil="true"></vendor>
            			<name>IPod Nano - 8gb - black</name>
          			</line-item>
        		</line-items>
        		<shipping-lines type="array">
          			<shipping-line>
            			<code>Free Shipping</code>
            			<price type="decimal">0.00</price>
            			<title>Free Shipping</title>
          			</shipping-line>
        		</shipping-lines>
        		<tax-lines type="array">
          			<tax-line>
            			<price type="decimal">11.94</price>
	            		<rate type="float">0.06</rate>
    	        		<title>State Tax</title>
        	  		</tax-line>
        		</tax-lines>
	        	<payment-details>
    	      		<credit-card-number>XXXX-XXXX-XXXX-4242</credit-card-number>
        	  		<credit-card-company>Visa</credit-card-company>
        		</payment-details>
        		<shipping-line>
          			<code>Free Shipping</code>
	          		<price type="decimal">0.00</price>
    	      		<title>Free Shipping</title>
        		</shipping-line>
        		<note-attributes type="array">
        		</note-attributes>
     		</order>
      		val expected = Order(false, //buyerAcceptsMarketing
        		None, //closedAt
        		parseDateTimeWithTimeZone("2008-01-10T11:00:00-05:00"), //createdAt
        		"USD", //currency
        		"bob.norman@hostmail.com", //email
        		1524531292, //id
        		"#1001", //name
        		1, //number
        		BigDecimal("199.00"), //subTotalPrice
        		BigDecimal("0.00"), //totalDiscounts
        		BigDecimal("199.00"), //totalLineItemsPrice
        		BigDecimal("210.94"), //totalPrice
        		BigDecimal("11.94"), //totalTax
        		0, //totalWeight
        		parseDateTimeWithTimeZone("2008-01-10T11:00:00-05:00"), //updatedAt
        		1001, //orderNumber
        		Address("Chestnut Street 92", "", "Louisville", None, "United States", "Bob", "Norman", "555-625-1199", "Kentucky", "40202", "Bob Norman", "US", "KY"), //billingAddress
        		Address("Chestnut Street 92", "", "Louisville", None, "United States", "Bob", "Norman", "555-625-1199", "Kentucky", "40202", "Bob Norman", "US", "KY"), //shippingAddress
        		LineItem("manual", None, 200, 703073503, BigDecimal("199.00"), Some(1706652214), 1, true, "IPOD2008BLACK", "IPod Nano - 8gb", Some(457924702), Some("black"), None, "IPod Nano - 8gb - black") :: Nil, //lineItems
        		ShippingLine("Free Shipping", BigDecimal("0.00"), "Free Shipping") :: Nil, //shippingLines
        		PaymentDetail("XXXX-XXXX-XXXX-4242", "Visa") //paymentDetail
        	)
      		val response = Order.parse(xmlToParse)
      		response must be_==(expected)
    	}
  	}
}
