package specs

import org.specs.Specification
import org.specs.runner.JUnit4

import org.joda.time.DateTime

import scala.{List, Nil}

import bizondemand.utils.models.internet.Url
import scala_mash.freshbooks_api.model._
import scala_mash.freshbooks_api.Utils._
import scala_mash.rest.util.Helpers._

object AutobillSpec extends Specification {

  "The Autobill class"  should {

		"be able to generate XML " in {
			card.toXml must ==/ ( xml)
		}

		"be able to parse XML " in {
			Autobill.parse( xml) must be_==( card)
		}
/*
		"be able to parse XML and return Some(autobill) when it's present " in {
			Autobill.optionalParse( xml) must beSome[Autobill]
		}

		"be able to parse XML and return None when it's not present " in {
			Autobill.optionalParse( <card><number>1234 1234 1234</number></card>) must beNone
		}
		*/
	}

	val xml =  
		<autobill> 
			<gateway_name>Authorize.net</gateway_name>  
			<card> <number>4111 1111 1111 1111</number>  
				<name>John Smith</name> 
				<expiration> 
					<month>3</month> 
					<year>2012</year> 
				</expiration> 
			</card> 
		</autobill>

	val card = new Autobill("Authorize.net", new CreditCard("4111 1111 1111 1111","John Smith",3, 2012))
}

