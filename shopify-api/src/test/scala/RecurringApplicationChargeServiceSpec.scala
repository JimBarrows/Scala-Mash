package specs

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

		shareVariables()
		setSequential()

		var charge: Option[ RecurringApplicationCharge] = None

		"be able to create a charge" in {
			val actual = RecurringApplicationCharge.create(ShopSpec.shopCredentials, 
					BigDecimal(100.00), 
					ShopSpec.shopname, 
					Url("http://localhost/returnUrl")
				)
			actual.name must  be_== (ShopSpec.shopname)
			actual.price must be_==(BigDecimal(100.00))
			charge = Some( actual)
		}

		"retrieve all charges" in {
			charge.map( recurringCharge => {
					val actual = RecurringApplicationCharge.list( ShopSpec.shopCredentials)
					actual must contain (recurringCharge)
				}
			)
		}

		"cancel a recurring charge" in {
			charge.map( recurringCharge => {
				RecurringApplicationCharge.cancel( ShopSpec.shopCredentials, recurringCharge.id.getOrElse(-1))
				RecurringApplicationCharge.list( ShopSpec.shopCredentials) must contain( recurringCharge)
			})
		}
  }
}
