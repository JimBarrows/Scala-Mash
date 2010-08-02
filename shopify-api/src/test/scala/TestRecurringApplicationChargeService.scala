package test

import org.specs.Specification
import org.joda.time.DateTime
import org.specs.runner.{JUnitSuiteRunner, JUnit4}
import org.junit.runner.RunWith
import scala_mash.shopify_api.model.{ShopCredentials, RecurringApplicationCharge, Shop}
import bizondemand.utils.models.internet.Url

/**
 * 
 * @author jimbarrows
 * @created: Feb 7, 2010 9:37:00 AM
 * @version 1.0
 * 
 */

object RecurringApplicationChargeServiceSpec extends Specification {
	"The Recurring application charge service " should {
		"be able to create a charge" in {
			val actual = RecurringApplicationCharge.create(ShopSpec.shopCredentials, 
					BigDecimal(100.00), 
					ShopSpec.shopname, 
					Url("http://localhost/returnUrl"))
			actual.name must be_== (ShopSpec.shopname)
			actual.price must be_==(BigDecimal(100.00))
	}
   "retrieve all charges" in {
	  val actual = RecurringApplicationCharge.list( ShopSpec.shopCredentials)
	  actual.size must be_>= (1)
	}
	"cancel a recurring charge" in {
	  val actual :List[RecurringApplicationCharge]= RecurringApplicationCharge.list( ShopSpec.shopCredentials)
	  actual.foreach(charge => RecurringApplicationCharge.cancel( ShopSpec.shopCredentials, charge.id.getOrElse(-1)))

	}
  }
}
