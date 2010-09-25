package test
import scala_mash.highrise_api.models.{PhoneNumber, EmailAddress, ContactData, Person}
import org.specs.Specification
import org.specs.runner.JUnit4
import scala_mash.highrise_api.Utils._

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

object PersonSpec extends Specification {
  "A Person class " should {
    "be able to parse a get response with a list of people in it" in {
      val xmlToParse =
      <people type="array">
        <person>
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
        </person>
        <person>
          <id type="integer">2</id>
          <first-name>Jane</first-name>
          <last-name>Smith</last-name>
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
        </person>
      </people>

      val expectedList = Person(Some(1l),
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
          )) :: Person(Some(2l),
        "Jane",
        "Smith",
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
          ))  :: Nil
      val actualList: List[Person] = Person.parseList(xmlToParse)

      actualList must containAll(expectedList)
    }
    "be able to display itself as XML" in {
      val expectedXml = <person>
        <first-name>John</first-name>
        <last-name>Doe</last-name>
        <title>Stand-in</title>
        <company-id type="integer">5</company-id>
        <id type="integer">1</id> <background>A popular guy for random data</background> <created-at type="datetime">2007-02-27T03:11:52Z</created-at> <updated-at type="datetime">2007-03-10T15:11:52Z</updated-at> <visible-to>Everyone</visible-to> <author-id type="integer">2</author-id> <contact-data>
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
      </person>

      val person = Person(Some(1l),
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
      expectedXml must ==/(person.toXml)
    }
  }
}
