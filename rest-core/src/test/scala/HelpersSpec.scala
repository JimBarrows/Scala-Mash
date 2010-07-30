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
	}	
}