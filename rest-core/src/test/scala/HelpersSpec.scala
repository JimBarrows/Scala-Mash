package test

import org.joda.time.{DateTime,DateTimeZone}

import org.specs.Specification

import scala_mash.rest.util.Helpers._

object HelpersSpec extends Specification {
	
	
	"Helpers" should {
		"be able to parse 2010-01-02T19:53:49Z" in {
			val expected = new DateTime(2010,01,02,19,53,49,0, DateTimeZone.forID("+00:00"))
			parseDateTimeWithTimeZone( "2010-01-02T19:53:49Z") must_== expected
		}
		"be able to format 2010-01-02T19:53:49Z" in {
			val expected = "2010-01-02T19:53:49Z"
			val date = new DateTime(2010,01,02,19,53,49,0, DateTimeZone.forID("+00:00"))
			printWithTimeZone(date) must_== expected
		}
		"be able to parse boolean values" in {
			optionalBoolean( <goof><boolean/></goof>, "boolean") must beNone			
			optionalBoolean( <goof><boolean>0</boolean></goof>, "boolean") must     beSome[Boolean].which( _ == false)
			optionalBoolean( <goof><boolean>1</boolean></goof>, "boolean") must     beSome[Boolean].which ( _ == true)
			optionalBoolean( <goof><boolean>f</boolean></goof>, "boolean") must     beSome[Boolean].which ( _ == false)
			optionalBoolean( <goof><boolean>t</boolean></goof>, "boolean") must     beSome[Boolean].which ( _ == true)
			optionalBoolean( <goof><boolean>F</boolean></goof>, "boolean") must     beSome[Boolean].which ( _ == false)
			optionalBoolean( <goof><boolean>T</boolean></goof>, "boolean") must     beSome[Boolean].which ( _ == true)
			optionalBoolean( <goof><boolean>false</boolean></goof>, "boolean") must beSome[Boolean].which ( _ == false)
			optionalBoolean( <goof><boolean>true</boolean></goof>, "boolean") must  beSome[Boolean].which ( _ == true)
		}
	}	
}