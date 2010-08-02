package test
import org.specs.Specification
import org.specs.runner.JUnit4

import org.joda.time.DateTime

import scala.{List, Nil}

import scala_mash.highrise_api.models._
import scala_mash.highrise_api.Utils._

/**
	* @author Jim Barrows
	* @created Feb 13, 2009
	* @version 1.0
	*/

object TagSpec extends Specification {

	"The tag class" should {
		"be able to generate xml" in {
			val expectedXml = <tag><id type="integer">703076</id><name>Test Contact</name></tag>
			Tag(Some(703076l),"Test Contact").toXml must equalIgnoreSpace (expectedXml)
		}

		"be able to parse a single tag in xml" in {
			val xmlToParse = <tag><id type="integer">703076</id><name>Test Contact</name></tag>
			Tag.parse(xmlToParse) must be_== (Tag(Some(703076l),"Test Contact"))
		}

		"be able to parse a list of tags in xml" in {
			val xmlToParse = <tags type="array"><tag><id type="integer">703076</id><name>Test Contact</name></tag><tag><id type="integer">703077</id><name>Test Contact 2</name></tag></tags>
			Tag.parseList(xmlToParse) must be_== (Tag(Some(703076l),"Test Contact") ::Tag(Some(703077l),"Test Contact 2") :: Nil)
		}
	}
}

