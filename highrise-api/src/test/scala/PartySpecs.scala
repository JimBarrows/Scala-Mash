package specs

import scala.{List, Nil}
import scala.xml._

import org.specs.Specification
import org.specs.runner.JUnit4

import scala_mash.highrise_api.models.{PhoneNumber, EmailAddress, ContactData, Company}
import scala_mash.highrise_api.Utils._
import scala_mash.highrise_api.models._
import scala_mash.highrise_api.models.enumerations._
import VisibleToValues._

import scala_mash.rest.util.Helpers._

object PartySpec extends Specification {

	"A Party class" should {
		"be able to parse a person from the parties services with the type as an attribute in the party tag" in {
			Party.parse( person1Xml) must be_== (person1)
		}

		"be able to parse a person from the parties services with the type as an element in the party node" in {
			Party.parse( person1aXml) must be_== (person1)
		}

		"be able to parse a company from the parties services, with the type as an attribute in the party tag" in {
			Party.parse( company1Xml) must be_== (company1)
		}

		"be able to parse a company from the parties services with the type as an element in the party node" in {
			Party.parse( company1aXml) must be_== (company1)
		}

		"be able to parse a list of companies and people from the parties services" in {
			Party.parseList( partiesXml) must be_== (company1 :: person1 :: Nil)
		}
	}

	val partiesXml = <parties type="array">
				<party type="Company">
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
					</party>
					<party type="Person">
          <id type="integer">1</id>
          <first-name>John</first-name>
          <last-name>Doe</last-name>
          <title>Stand-in</title>
          <background>A popular guy for random data</background>
          <company-id type="integer">5</company-id>
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
        </party>
        </parties>

	val company1 = Company(Some(1l),
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
          )
  ) 

	val company1Xml = 	<party type="Company">
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
						</party>

	val company1aXml = 	<party >
						<type>Company</type>
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
						</party>

	val person1 = Person(Some(1l),
        "John",
        "Doe",
        "Stand-in",
        Some("A popular guy for random data"),
        Some(5),
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
          )) 

	val person1Xml = <party type="Person">
          <id type="integer">1</id>
          <first-name>John</first-name>
          <last-name>Doe</last-name>
          <title>Stand-in</title>
          <background>A popular guy for random data</background>
          <company-id type="integer">5</company-id>
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
        </party>

	val person1aXml = <party>
					<type>Person</type>
          <id type="integer">1</id>
          <first-name>John</first-name>
          <last-name>Doe</last-name>
          <title>Stand-in</title>
          <background>A popular guy for random data</background>
          <company-id type="integer">5</company-id>
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
        </party>
}
