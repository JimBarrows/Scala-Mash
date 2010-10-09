package specs
import scala_mash.highrise_api.models.{PhoneNumber, EmailAddress, ContactData, Company}
import org.specs.Specification
import org.specs.runner.JUnit4

import scala_mash.highrise_api.models.enumerations._
import VisibleToValues._

import scala.{List, Nil}

import scala_mash.rest.util.Helpers._

/**
 *
 * @author jimbarrows
 * @created: Dec 25, 2009 11:35:42 AM
 * @version 1.0
 *
 */

object CompanySpec extends Specification {
  "A Company class " should {

  	"replace spaces in the name with dashes for the apiId" in {
  		Company(Some(1),"This hasOneSpace", None, None, None, None, None, None, None, 
  			ContactData(None,None,None,None,None)).apiId must be_==("1-this-hasonespace")
  		Company(Some(1),"This has One Space", None, None, None, None, None, None, None, 
  			ContactData(None,None,None,None,None)).apiId must be_==("1-this-has-one-space")
  		Company(Some(1),"This  has  Two  Space", None, None, None, None, None, None, None, 
  			ContactData(None,None,None,None,None)).apiId must be_==("1-this--has--two--space")
  		}

    "be able to parse a get response with a list of companies in it" in {
			val xmlToParse =
				<companies type="array">
					<company>
						<id type="integer">1</id>
						<name>John</name>
						<background>A popular guy for random data</background>
						<created-at type="datetime">2007-02-27T03:11:52Z</created-at>
						<updated-at type="datetime">2007-03-10T15:11:52Z</updated-at>
						<visible-to>Everyone</visible-to>
						<owner-id type="integer"></owner-id>
						<group-id type="integer"></group-id>
						<author-id type="integer">2</author-id>
						<contact-data>
							<email-addresses>
								<email-address>
									<id type="integer">1</id>
									<address>john.doe@example.com</address>
									<location>Work</location>
								</email-address>
							</email-addresses>
							<phone-numbers>
								<phone-number>
									<id type="integer">2</id>
										<number>555-555-5555</number>
										<location>Work</location>
								</phone-number>
								<phone-number>
									<id type="integer">3</id>
									<number>555-666-6666</number>
									<location>Home</location>
 								</phone-number>
							</phone-numbers>
						</contact-data>
						</company>
						<company>
						<id type="integer">2</id>
						<name>Jane</name>
						<background>A popular guy for random data</background>
						<created-at type="datetime">2007-02-27T03:11:52Z</created-at>
						<updated-at type="datetime">2007-03-10T15:11:52Z</updated-at>
						<visible-to>Everyone</visible-to>
						<owner-id type="integer"></owner-id>
						<group-id type="integer"></group-id>
						<author-id type="integer">2</author-id>
						<contact-data>
						<email-addresses>
						<email-address>
						<id type="integer">1</id>
						<address>john.doe@example.com</address>
						<location>Work</location>
						</email-address>
						</email-addresses>
						<phone-numbers>
						<phone-number>
						<id type="integer">2</id>
						<number>555-555-5555</number>
						<location>Work</location>
						</phone-number>
						<phone-number>
						<id type="integer">3</id>
						<number>555-666-6666</number>
						<location>Home</location>
						</phone-number>
						</phone-numbers>
						</contact-data>
						</company>
					</companies>

      val expectedList = Company(Some(1l),
        "John",
        Some("A popular guy for random data"),
        Some(parseDateTimeWithTimeZone("2007-02-27T03:11:52Z")),
        Some(parseDateTimeWithTimeZone("2007-03-10T15:11:52Z")),
        Some(Everyone),
        None,
        None,
        Some(2),
        ContactData(
          Some(EmailAddress(Some(1), "john.doe@example.com", scala_mash.highrise_api.models.AddressLocationValues.Work) :: Nil),
          Some(PhoneNumber(Some(2), "555-555-5555", scala_mash.highrise_api.models.PhoneLocationValues.Work) :: PhoneNumber(Some(3), "555-666-6666", scala_mash.highrise_api.models.PhoneLocationValues.Home) :: Nil),
          None,
          None,
          None
          )) :: Company(Some(2l),
        "Jane",
        Some("A popular guy for random data"),
        Some(parseDateTimeWithTimeZone("2007-02-27T03:11:52Z")),
        Some(parseDateTimeWithTimeZone("2007-03-10T15:11:52Z")),
        Some(Everyone),
        None,
        None,
        Some(2),
        ContactData(
          Some(EmailAddress(Some(1), "john.doe@example.com", scala_mash.highrise_api.models.AddressLocationValues.Work) :: Nil),
          Some(PhoneNumber(Some(2), "555-555-5555", scala_mash.highrise_api.models.PhoneLocationValues.Work) :: PhoneNumber(Some(3), "555-666-6666", scala_mash.highrise_api.models.PhoneLocationValues.Home) :: Nil),
          None,
          None,
          None
          ))  :: Nil
      val actualList: List[Company] = Company.parseList(xmlToParse)

      actualList must containAll(expectedList)
    }
    "be able to display itself as XML" in {
      val expectedXml = <company>
        <name>John</name>
        <id type="integer">1</id>
        <background>A popular guy for random data</background>
        <created-at type="datetime">2007-02-27T03:11:52Z</created-at>
        <updated-at type="datetime">2007-03-10T15:11:52Z</updated-at>
        <visible-to>Everyone</visible-to>
        <author-id type="integer">2</author-id>
        <contact-data>
          <email-addresses>
            <email-address>
              <id type="integer">1</id>
              <address>john.doe@example.com</address>
              <location>Work</location>
            </email-address>
          </email-addresses>
          <phone-numbers>
            <phone-number>
              <id type="integer">2</id>
              <number>555-555-5555</number>
              <location>Work</location>
            </phone-number>
            <phone-number>
              <id type="integer">3</id>
              <number>555-666-6666</number>
              <location>Home</location>
            </phone-number>
          </phone-numbers>
        </contact-data>
      </company>

      val company = Company(Some(1l), //id
        "John",  //name
        Some("A popular guy for random data"),  //background
        Some(parseDateTimeWithTimeZone("2007-02-27T03:11:52Z")), // created
        Some(parseDateTimeWithTimeZone("2007-03-10T15:11:52Z")), // updated
        Some(Everyone), //visible to
        None,  //owner
        None,  //group
        Some(2),  //author
        ContactData(
          Some(EmailAddress(Some(1), "john.doe@example.com", scala_mash.highrise_api.models.AddressLocationValues.Work) :: Nil),
          Some(PhoneNumber(Some(2), "555-555-5555", scala_mash.highrise_api.models.PhoneLocationValues.Work) :: PhoneNumber(Some(3), "555-666-6666", scala_mash.highrise_api.models.PhoneLocationValues.Home) :: Nil),
          None,
          None,
          None
          ))
      expectedXml must ==/ (company.toXml)
    }
  }
}
