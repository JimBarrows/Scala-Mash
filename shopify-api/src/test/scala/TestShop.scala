package specs

import org.specs.runner.JUnit4
import org.specs.Specification
import scala_mash.shopify_api.Utils._
import scala_mash.shopify_api.ShopifyPartnerInfo
import scala_mash.shopify_api.model.{ShopCredentials, Shop}
import scala_mash.rest.util.Helpers._

/**
 *
 * @author jimbarrows
 * @created: Feb 6, 2010 2:42:02 PM
 * @version 1.0
 *
 */
object ShopSpec extends Specification {

  val shopname = "mante-friesen-and-kuvalis9996"
  val authenticationToken = "4aa847d832027e5f883369f60102d989"
  val signature = ShopifyPartnerInfo.createPasswordForStore(authenticationToken)
  val shopCredentials = ShopCredentials( shopname, signature, authenticationToken)

  "The shop class" should {
    "be able to parse shop xml" in {
  val expectedShop = Shop(None,
        "185 Rideau Street",
        "Boston",
        "US",
        parseDateTimeWithTimeZone("2010-01-31T10:11:46-05:00"),
        "mante-friesen-and-kuvalis9996.myshopify.com",
        "Jim.Barrows@bizondemand.biz",
        435932l,
        "Mante, Friesen and Kuvalis",
        "555 555 5555",
        "Ontario",
        false,
        None,
        "K1N 5X8",
        "USD",
        "(GMT-05:00) Eastern Time (US & Canada)",
        "development shop",
        "${{amount}}",
        "${{amount}} USD",
        None,
        None,
        "development"
        )
      val xmlToParse = <shop>
        <address1>185 Rideau Street</address1>
        <city>Boston</city>
        <country>US</country>
        <created-at type="datetime">2010-01-31T10:11:46-05:00</created-at>
        <domain>mante-friesen-and-kuvalis9996.myshopify.com</domain>
        <email>Jim.Barrows@bizondemand.biz</email>
        <id type="integer">435932</id>
        <name>Mante, Friesen and Kuvalis</name>
        <phone>555 555 5555</phone>
        <province>Ontario</province>
        <public type="boolean">false</public>
        <source nil="true"></source>
        <zip>K1N 5X8</zip>
        <currency>USD</currency>
        <timezone>(GMT-05:00) Eastern Time (US &amp; Canada)</timezone>
        <shop-owner>development shop</shop-owner>
        <money-format>${{{{amount}}}}</money-format>
        <money-with-currency-format>${{{{amount}}}} USD</money-with-currency-format>
        <taxes-included nil="true"></taxes-included>
        <tax-shipping nil="true"></tax-shipping>
        <plan-name>development</plan-name>
      </shop>



      val actualShop = Shop.parseShop(xmlToParse)
      actualShop must be_== (expectedShop)
		}
    "be able to parse shop xml, with taxes-include and tax-shipping set to 0" in {
			val expectedShop = Shop(None,
        "185 Rideau Street",
        "Boston",
        "US",
        parseDateTimeWithTimeZone("2010-01-31T10:11:46-05:00"),
        "mante-friesen-and-kuvalis9996.myshopify.com",
        "Jim.Barrows@bizondemand.biz",
        435932l,
        "Mante, Friesen and Kuvalis",
        "555 555 5555",
        "Ontario",
        false,
        None,
        "K1N 5X8",
        "USD",
        "(GMT-05:00) Eastern Time (US & Canada)",
        "development shop",
        "${{amount}}",
        "${{amount}} USD",
        Some(false),
        Some(false),
        "development"
        )
      val xmlToParse = <shop>
        <address1>185 Rideau Street</address1>
        <city>Boston</city>
        <country>US</country>
        <created-at type="datetime">2010-01-31T10:11:46-05:00</created-at>
        <domain>mante-friesen-and-kuvalis9996.myshopify.com</domain>
        <email>Jim.Barrows@bizondemand.biz</email>
        <id type="integer">435932</id>
        <name>Mante, Friesen and Kuvalis</name>
        <phone>555 555 5555</phone>
        <province>Ontario</province>
        <public type="boolean">false</public>
        <source nil="true"></source>
        <zip>K1N 5X8</zip>
        <currency>USD</currency>
        <timezone>(GMT-05:00) Eastern Time (US &amp; Canada)</timezone>
        <shop-owner>development shop</shop-owner>
        <money-format>${{{{amount}}}}</money-format>
        <money-with-currency-format>${{{{amount}}}} USD</money-with-currency-format>
        <taxes-included>0</taxes-included>
        <tax-shipping>0</tax-shipping>
        <plan-name>development</plan-name>
      </shop>



      val actualShop = Shop.parseShop(xmlToParse)
      actualShop must be_== (expectedShop)
		}
    "be able to parse shop xml, with taxes-included and tax-shipping set to 1" in {
			val expectedShop = Shop(None,
        "185 Rideau Street",
        "Boston",
        "US",
        parseDateTimeWithTimeZone("2010-01-31T10:11:46-05:00"),
        "mante-friesen-and-kuvalis9996.myshopify.com",
        "Jim.Barrows@bizondemand.biz",
        435932l,
        "Mante, Friesen and Kuvalis",
        "555 555 5555",
        "Ontario",
        false,
        None,
        "K1N 5X8",
        "USD",
        "(GMT-05:00) Eastern Time (US & Canada)",
        "development shop",
        "${{amount}}",
        "${{amount}} USD",
        Some(true),
        Some(true),
        "development"
        )
      val xmlToParse = <shop>
        <address1>185 Rideau Street</address1>
        <city>Boston</city>
        <country>US</country>
        <created-at type="datetime">2010-01-31T10:11:46-05:00</created-at>
        <domain>mante-friesen-and-kuvalis9996.myshopify.com</domain>
        <email>Jim.Barrows@bizondemand.biz</email>
        <id type="integer">435932</id>
        <name>Mante, Friesen and Kuvalis</name>
        <phone>555 555 5555</phone>
        <province>Ontario</province>
        <public type="boolean">false</public>
        <source nil="true"></source>
        <zip>K1N 5X8</zip>
        <currency>USD</currency>
        <timezone>(GMT-05:00) Eastern Time (US &amp; Canada)</timezone>
        <shop-owner>development shop</shop-owner>
        <money-format>${{{{amount}}}}</money-format>
        <money-with-currency-format>${{{{amount}}}} USD</money-with-currency-format>
        <taxes-included>1</taxes-included>
        <tax-shipping>1</tax-shipping>
        <plan-name>development</plan-name>
      </shop>



      val actualShop = Shop.parseShop(xmlToParse)
      actualShop must be_== (expectedShop)
		}
  }
}
