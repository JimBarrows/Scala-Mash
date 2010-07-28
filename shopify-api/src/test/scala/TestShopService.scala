package test

import org.specs.Specification
import org.specs.runner.JUnit4
import scala_mash.shopify_api.model.{ShopCredentials, Shop}
import scala_mash.shopify_api.ShopifyPartnerInfo
import scala_mash.shopify_api.Utils._

/**
 *
 * @author jimbarrows
 * @created: Feb 6, 2010 3:08:38 PM
 * @version 1.0
 *
 */

class TestShopService extends JUnit4(ShopServiceSpec)
object ShopServiceSpec extends Specification {
  "The shop class" should {
    "be able to retrieve a shop's info from the shop service" in {
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
        Some(true),
        "development"
        )

      val actual = Shop.findShop(ShopSpec.shopCredentials)

      actual must be_== (expectedShop)
    }
  }
}
