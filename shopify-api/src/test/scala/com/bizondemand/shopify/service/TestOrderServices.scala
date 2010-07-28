package test

import org.specs.runner.JUnit4
import org.specs.Specification
import org.joda.time.{Interval, DateTime}
import com.nsfw.shopifyapp.shopify.ShopifyPartnerInfo
import com.nsfw.shopifyapp.shopify.model.{ShopCredentials, Order, Shop}

/**
 *
 * @author jimbarrows
 * @created: Dec 22, 2009 8:19:20 PM
 * @version 1.0
 *
 */
object OrderServicesSpec extends Specification {
  
	"The Order Services" should {
		"Receive a list of all orders" in {
    		val actual = Order.findAllOrders(ShopSpec.shopCredentials)
			actual must notBeEmpty
		}
		"Receive a list of all orders created/updated after a date" in { 
			val actual = Order.findAllOrdersCreatedSince(ShopSpec.shopCredentials, new DateTime().minusYears(2))
			actual must notBeEmpty
		}
	}
}
