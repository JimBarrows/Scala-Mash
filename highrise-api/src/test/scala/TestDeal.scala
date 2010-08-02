package test
import org.specs.Specification
import org.specs.runner.JUnit4

import org.joda.time._

import scala.{List, Nil}

import scala_mash.highrise_api.models._
import scala_mash.highrise_api.Utils._
import scala_mash.highrise_api.models.enumerations.VisibleToValues._
import scala_mash.highrise_api.models.enumerations.DealStatus._

object DealSpec extends Specification {

	"The deal class " should {
		"generate a correct xml for pending and fixed deals" in {	
			pendingFixedDeal.toXml must equalIgnoreSpace(pendingFixedDealXml)
		}
		
		"generate a correct xml for won and per hour deal" in {
			wonPerHourDeal.toXml must equalIgnoreSpace(wonPerHourDealXml)
		}
		
		"generate a correct xml for lost per month deal" in {
			lostPerMonth.toXml must equalIgnoreSpace(lostPerMonthXml)
		}
		
		"parse an xml correctly for pending and fixed deals" in {
			Deal.parse(pendingFixedDealXml) must equalTo ( pendingFixedDeal)
		}
		
		"parse an xml correctly for won and per hour deals" in {
			Deal.parse( wonPerHourDealXml) must equalTo(wonPerHourDeal)
		}
		
		"parse an cml correctly for lost per month deals" in {
			Deal.parse( lostPerMonthXml) must equalTo(lostPerMonth)
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
