package test

import org.specs.Specification
import java.util.Date
import org.joda.time.DateTime
import org.specs.runner.{JUnitSuiteRunner, JUnit4}
import org.junit.runner.RunWith
import com.nsfw.shopifyapp.shopify.model.{ShopCredentials, ApplicationCharge, Shop}
import bizondemand.utils.models.internet.{Url,DomainName,Http}


/**
 *
 * @author jimbarrows
 * @created: Dec 14, 2009 9:00:47 PM
 * @version 1.0
 *
 */
object ApplicationChargeServiceSpec extends Specification {
	"The application charge service" should {
		"return a proper response" in {
    		val now :DateTime = new DateTime()
      		val chargeInfo :ApplicationCharge = new ApplicationCharge(
      			ShopSpec.shopname,																						//name: String,
				BigDecimal("5.00"),																						//price: BigDecimal,
				Url(Http(), None, None, DomainName("localhost"::Nil),Some(8080),Some("/chargeResult"::Nil), None),	//returnUrl: Url,
				None, 																									//createdAt: Option[DateTime],
				None,																									//id: Option[Int],
				None,																									//status: Option[String],
				true,																									//test: Boolean,
				None,																									//updatedAt: Option[DateTime],
				Some(Url("http://confirm"))																				//confirmationUrl: Option[Url]
        	)

     		val actual = ApplicationCharge.charge(ShopSpec.shopCredentials, chargeInfo.name, chargeInfo.price, chargeInfo.returnUrl,chargeInfo.test)

      		actual.id must notBeEmpty
      		actual.name must be_==(chargeInfo.name)
      		actual.price must be_==(chargeInfo.price)
      		actual.status must notBeEmpty
      		actual.updatedAt must notBeEmpty
    }
  }

}
