package specs
import org.specs.Specification
import org.specs.runner.JUnit4

import org.joda.time._

import scala.{List, Nil}

import scala_mash.highrise_api._
import models._
import enumerations.VisibleToValues._
import enumerations.DealStatus._

import scala_mash.rest.util.Helpers._

object DealSpec extends Specification {

	"The deal class " should {
		"generate a correct xml for pending and fixed deals" in {	
			pendingFixedDeal.toXml must ==/(pendingFixedDealXml)
		}
		
		"generate a correct xml for won and per hour deal" in {
			wonPerHourDeal.toXml must ==/(wonPerHourDealXml)
		}

		"generate a correct xml for lost per month deal" in {
			lostPerMonth.toXml must ==/(lostPerMonthXml)
		}

		"parse an xml correctly for pending and fixed deals" in {
			Deal.parse(pendingFixedDealXml) must equalTo ( pendingFixedDeal)
		}
		
		"parse an xml correctly for won and per hour deals" in {
			Deal.parse( wonPerHourDealXml) must equalTo(wonPerHourDeal)
		}
		
		"parse an xml correctly for lost per month deals" in {
			Deal.parse( lostPerMonthXml) must equalTo(lostPerMonth)
		}

		"parse an xml correctly with a company for a party" in {
			Deal.parse( withCompanyXml) must equalTo(withCompany)
		}
	}
	
	val pendingFixedDeal = Deal(Some(109504l), 									//accountId
		Some(214479l),  														//authorId
		Some("This is a test deal."), 											//background
		Some(1660938l), 														//categoryId
		Some(new DateTime(2010,5,9,20,44,38,0, DateTimeZone.forID("+00:00"))), 	//createdAt
		Some("USD"), 															//currency
		None, 																	//duration
		None,   																//groupId
		Some(412063l), 															//id
		"Pending Fixed Deal", 													//name
		Some(214479), 															//ownerId
		Some(37932948), 														//partyId
		Some(100),																//price
		Some("fixed"), 															//priceType
		Some(214479l),															//responsiblePartyId
		Some(Pending),															//status
		None,																	//statusChangedOn
		Some(new DateTime(2010,5,9,20,45,6,0, DateTimeZone.forID("+00:00"))),	//updatedAt
		Some(Owner),															//visibleTo
		Some(DealCategory(Some(1660935l), "Print Project", None, None, None, None))	//dealCategory
		,None
		,None
		)
	
	val pendingFixedDealXml =  <deal>
    <account-id type="integer">109504</account-id>
    <author-id type="integer">214479</author-id>
    <background>This is a test deal.</background>
    <category-id type="integer">1660938</category-id>
    <created-at type="datetime">2010-05-09T20:44:38Z</created-at>
    <currency>USD</currency>
    <duration type="integer"></duration>
    <group-id type="integer"></group-id>
    <id type="integer">412063</id>
    <name>Pending Fixed Deal</name>
    <owner-id type="integer">214479</owner-id>
    <party-id type="integer">37932948</party-id>
    <price type="integer">100</price>
    <price-type>fixed</price-type>
    <responsible-party-id type="integer">214479</responsible-party-id>
    <status>pending</status>
    <status-changed-on type="date"></status-changed-on>
    <updated-at type="datetime">2010-05-09T20:45:06Z</updated-at>
    <visible-to>Owner</visible-to>
    <category>
    	<id type="integer">1660935</id>
    	<name>Print Project</name>
  	</category>
  </deal>


	val wonPerHourDeal = Deal(Some(109504l),									//acountId
		Some(214479l),															//authorId
		Some("Another Test Deal"),												//background
		Some(1660935l),															//categoryId
		Some(new DateTime(2010,5,9,20,56,27,0, DateTimeZone.forID("+00:00"))),  //createdAt
		Some("USD"), 															//currency
		Some(10l), 																//duration
		None,																	//groupId
		Some(412069l),															//id
		"Won Per hour",															//name
		Some(214479),															//ownerId
		Some(37932948),															//partyId
		None,																	//price
		Some("hour"),															//priceType
		Some(214479l),															//responsiblePartyId
		Some(Won),																//status
		Some( new LocalDate(2010,5,9)),											//statusChangedOn
		Some(new DateTime(2010,5,9,20,56,46,0, DateTimeZone.forID("+00:00"))),  //updatedAt
		Some(Owner),															//visibleTo
		Some(DealCategory(Some(1660935l), "Print Project", None, None, None, None))  //dealCategory
		,None
		,None
		)
	val wonPerHourDealXml = <deal>
  <account-id type="integer">109504</account-id>
  <author-id type="integer">214479</author-id>
  <background>Another Test Deal</background>
  <category-id type="integer">1660935</category-id>
  <created-at type="datetime">2010-05-09T20:56:27Z</created-at>
  <currency>USD</currency>
  <duration type="integer">10</duration>
  <group-id type="integer"></group-id>
  <id type="integer">412069</id>
  <name>Won Per hour</name>
  <owner-id type="integer">214479</owner-id>
  <party-id type="integer">37932948</party-id>
  <price type="integer" ></price>
  <price-type>hour</price-type>
  <responsible-party-id type="integer">214479</responsible-party-id>
  <status>won</status>
  <status-changed-on type="date">2010-05-09</status-changed-on>
  <updated-at type="datetime">2010-05-09T20:56:46Z</updated-at>
  <visible-to>Owner</visible-to>
  <category>
    <id type="integer">1660935</id>
    <name>Print Project</name>
  </category>
</deal>

val lostPerMonth = Deal(Some(109504l),		//acountId
		Some(214479l),						//authorId
		None,								//background
		Some(1660938l),						//categoryId
		Some(new DateTime(2010,5,9,20,58,49,0, DateTimeZone.forID("+00:00"))),  //createdAt
		Some("USD"), 							//currency
		Some(10l), 						//duration
		None,							//groupId
		Some(412073l),						//id
		"Lost Per Month",					//name
		Some(214479),							//ownerId
		Some(37932948),						//partyId
		Some(100l),							//price
		Some("month"),							//priceType
		Some(214479l),						//responsiblePartyId
		Some(Lost),							//status
		Some( new LocalDate(2010,5,9)),		//statusChangedOn
		Some(new DateTime(2010,5,9,20,59,0,0, DateTimeZone.forID("+00:00"))),  //updatedAt
		Some( Owner),							//visibleTo
		Some( DealCategory(Some(1660938l), "Copywriting", None, None, None, None))  //dealCategory
		,None
		,None
		)
	val lostPerMonthXml = <deal>
  <account-id type="integer">109504</account-id>
  <author-id type="integer">214479</author-id>
  <background></background>
  <category-id type="integer">1660938</category-id>
  <created-at type="datetime">2010-05-09T20:58:49Z</created-at>
  <currency>USD</currency>
  <duration type="integer">10</duration>
  <group-id type="integer"></group-id>
  <id type="integer">412073</id>
  <name>Lost Per Month</name>
  <owner-id type="integer">214479</owner-id>
  <party-id type="integer">37932948</party-id>
  <price type="integer">100</price>
  <price-type>month</price-type>
  <responsible-party-id type="integer">214479</responsible-party-id>
  <status>lost</status>
  <status-changed-on type="date">2010-05-09</status-changed-on>
  <updated-at type="datetime">2010-05-09T20:59:00Z</updated-at>
  <visible-to>Owner</visible-to>
  <category>
    <id type="integer">1660938</id>
    <name>Copywriting</name>
  </category>
	</deal>

	val withCompany = Deal(Some(109504l),		//acountId
		Some(214479l),						//authorId
		Some("Another test account"),								//background
		Some(1660937l),						//categoryId
		Some(new DateTime(2010,5,9,21,01,21,0, DateTimeZone.forID("+00:00"))),  //createdAt
		Some("USD"), 							//currency
		Some(10l), 						//duration
		None,							//groupId
		Some(412079l),						//id
		"Pending Per Year",					//name
		Some(214479),							//ownerId
		Some(37932948),						//partyId
		Some(100l),							//price
		Some("year"),							//priceType
		Some(214479l),						//responsiblePartyId
		Some(Pending),							//status
		None,		//statusChangedOn
		Some(new DateTime(2010,5,9,21,01,21,0, DateTimeZone.forID("+00:00"))),  //updatedAt
		Some( Owner),							//visibleTo
		Some( DealCategory(Some(1660937l), "Strategy Consulting", None, None, None, None)),  //dealCategory
		Some( Company(Some(1l),
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

          )))
		,None
		)

	val withCompanyXml = <deal>
  <account-id type="integer">109504</account-id>
  <author-id type="integer">214479</author-id>
  <background>Another test account</background>
  <category-id type="integer">1660937</category-id>
  <created-at type="datetime">2010-05-09T21:01:21Z</created-at>
  <currency>USD</currency>
  <duration type="integer">10</duration>
  <group-id type="integer"></group-id>
  <id type="integer">412079</id>
  <name>Pending Per Year</name>
  <owner-id type="integer">214479</owner-id>
  <party-id type="integer">37932948</party-id>
  <price type="integer">100</price>
  <price-type>year</price-type>
  <responsible-party-id type="integer">214479</responsible-party-id>
  <status>pending</status>
  <status-changed-on type="date"></status-changed-on>
  <updated-at type="datetime">2010-05-09T21:01:21Z</updated-at>
  <visible-to>Owner</visible-to>
  <category>
    <id type="integer">1660937</id>
    <name>Strategy Consulting</name>
  </category>
	<party>
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
</deal>

	val pendingPerYearXml = <deal>
  <account-id type="integer">109504</account-id>
  <author-id type="integer">214479</author-id>
  <background>Another test account</background>
  <category-id type="integer">1660937</category-id>
  <created-at type="datetime">2010-05-09T21:01:21Z</created-at>
  <currency>USD</currency>
  <duration type="integer">10</duration>
  <group-id type="integer"></group-id>
  <id type="integer">412079</id>
  <name>Pending Per Year</name>
  <owner-id type="integer">214479</owner-id>
  <party-id type="integer">37932948</party-id>
  <price type="integer">100</price>
  <price-type>year</price-type>
  <responsible-party-id type="integer">214479</responsible-party-id>
  <status>pending</status>
  <status-changed-on type="date"></status-changed-on>
  <updated-at type="datetime">2010-05-09T21:01:21Z</updated-at>
  <visible-to>Owner</visible-to>
  <category>
    <id type="integer">1660937</id>
    <name>Strategy Consulting</name>
  </category>
</deal>


	val dealList = <deals type="array">
  <deal>
    <account-id type="integer">109504</account-id>
    <author-id type="integer">214479</author-id>
    <background></background>
    <category-id type="integer">1660938</category-id>
    <created-at type="datetime">2010-05-09T20:58:49Z</created-at>
    <currency>USD</currency>
    <duration type="integer">10</duration>
    <group-id type="integer"></group-id>
    <id type="integer">412073</id>
    <name>Lost Per Month</name>
    <owner-id type="integer">214479</owner-id>
    <party-id type="integer">37932948</party-id>
    <price type="integer">100</price>
    <price-type>month</price-type>
    <responsible-party-id type="integer">214479</responsible-party-id>
    <status>lost</status>
    <status-changed-on type="date">2010-05-09</status-changed-on>
    <updated-at type="datetime">2010-05-09T20:59:00Z</updated-at>
    <visible-to>Owner</visible-to>
    <category>
      <id type="integer">1660938</id>
      <name>Copywriting</name>
    </category>
  </deal>
  <deal>
    <account-id type="integer">109504</account-id>
    <author-id type="integer">214479</author-id>
    <background>This is a test deal.</background>
    <category-id type="integer">1660938</category-id>
    <created-at type="datetime">2010-05-09T20:44:38Z</created-at>
    <currency>USD</currency>
    <duration type="integer"></duration>
    <group-id type="integer"></group-id>
    <id type="integer">412063</id>
    <name>Pending Fixed Deal</name>
    <owner-id type="integer">214479</owner-id>
    <party-id type="integer">37932948</party-id>
    <price type="integer">100</price>
    <price-type>fixed</price-type>
    <responsible-party-id type="integer">214479</responsible-party-id>
    <status>pending</status>
    <status-changed-on type="date"></status-changed-on>
    <updated-at type="datetime">2010-05-09T20:45:06Z</updated-at>
    <visible-to>Owner</visible-to>
    <category>
      <id type="integer">1660938</id>
      <name>Copywriting</name>
    </category>
  </deal>
  <deal>
    <account-id type="integer">109504</account-id>
    <author-id type="integer">214479</author-id>
    <background>Another test account</background>
    <category-id type="integer">1660937</category-id>
    <created-at type="datetime">2010-05-09T21:01:21Z</created-at>
    <currency>USD</currency>
    <duration type="integer">10</duration>
    <group-id type="integer"></group-id>
    <id type="integer">412079</id>
    <name>Pending Per Year</name>
    <owner-id type="integer">214479</owner-id>
    <party-id type="integer">37932948</party-id>
    <price type="integer">100</price>
    <price-type>year</price-type>
    <responsible-party-id type="integer">214479</responsible-party-id>
    <status>pending</status>
    <status-changed-on type="date"></status-changed-on>
    <updated-at type="datetime">2010-05-09T21:01:21Z</updated-at>
    <visible-to>Owner</visible-to>
    <category>
      <id type="integer">1660937</id>
      <name>Strategy Consulting</name>
    </category>
  </deal>
  <deal>
    <account-id type="integer">109504</account-id>
    <author-id type="integer">214479</author-id>
    <background>Another Test Deal</background>
    <category-id type="integer">1660935</category-id>
    <created-at type="datetime">2010-05-09T20:56:27Z</created-at>
    <currency>USD</currency>
    <duration type="integer">10</duration>
    <group-id type="integer"></group-id>
    <id type="integer">412069</id>
    <name>Won Per hour</name>
    <owner-id type="integer">214479</owner-id>
    <party-id type="integer">37932948</party-id>
    <price type="integer"></price>
    <price-type>hour</price-type>
    <responsible-party-id type="integer">214479</responsible-party-id>
    <status>won</status>
    <status-changed-on type="date">2010-05-09</status-changed-on>
    <updated-at type="datetime">2010-05-09T20:56:46Z</updated-at>
    <visible-to>Owner</visible-to>
    <category>
      <id type="integer">1660935</id>
      <name>Print Project</name>
    </category>
  </deal>
	</deals>


}
