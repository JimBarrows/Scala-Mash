package specs

import org.specs.Specification
import org.specs.runner.JUnit4

import org.joda.time.DateTime

import scala.{List, Nil}

import bizondemand.utils.models.internet.Url
import scala_mash.freshbooks_api.model._
import scala_mash.freshbooks_api.Utils._
import scala_mash.rest.util.Helpers._

object CreditCardSpec extends Specification {

  "The CreditCard class"  should {

		"be able to generate XML " in {
			card.toXml must ==/ ( xml)
		}

		"be able to parse XML " in {
			CreditCard.parse( xml) must be_==( card)
		}
	}

	val xml =  <card>
			<number>4111 1111 1111 1111</number>  
			<name>John Smith</name>
			<expiration>
				<month>3</month>
				<year>2012</year>
			</expiration>
		</card>

	val card = new CreditCard("4111 1111 1111 1111","John Smith",3, 2012)
}

