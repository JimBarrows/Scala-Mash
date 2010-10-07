package specs

import org.specs.Specification
import org.specs.runner.JUnit4

import org.joda.time.{DateTime,LocalDate}

import scala.{List, Nil}

import bizondemand.utils.models.internet.Url
import scala_mash.freshbooks_api.model._
import scala_mash.freshbooks_api.Utils._
import scala_mash.rest.util.Helpers._
import Frequency._

object RecurringInvoiceSpec extends Specification {

  "The RecurringInvoice class"  should {
/*
		"be able to generate XML " in {
			card.toXml must ==/ ( xml)
		}

		"be able to parse XML " in {
			val parsedCard = RecurringInvoice.parse( xml) 
			parsedCard must be_==( card)
		}

		"be able to parse XML without autobill " in {
			val parsedCard = RecurringInvoice.parse( xml) 
			parsedCard must be_==( card)
		}
*/
		"be able to parse xml from freshbooks" in {
			val parsedFreshbooks = RecurringInvoice.parse( freshbooksXml)
			println("freshbooks:       %s".format(freshbooks))
			println("parsedFreshbooks: %s".format(parsedFreshbooks))
			parsedFreshbooks must be_==( freshbooks)
		}
	}


	val freshbooksXml = <recurring>
    <recurring_id>00000000030</recurring_id>
    <frequency>m</frequency>
    <occurrences>0</occurrences>
    <stopped>0</stopped>
    <currency_code>CAD</currency_code>
    <autobill></autobill>
    <client_id>2</client_id>
    <organization>ABC Corp</organization>
    <first_name>John</first_name>
    <last_name>Smith</last_name>
    <p_street1>Street 1</p_street1>
    <p_street2>Street 2</p_street2>
    <p_city>City</p_city>
    <p_state>State</p_state>
    <p_country>Country</p_country>
    <p_code>Code</p_code>
    <vat_name>Vat Name</vat_name>
    <vat_number>123456</vat_number>
    <language>en</language>
    <po_number>2314</po_number>
    <status></status>
    <amount>40.68</amount>
    <date>2010-10-07</date>
    <notes>Due upon receipt.</notes>
    <terms>Payment due in 30 days.</terms>
    <discount>10</discount>
    <return_uri>http://example.com/account</return_uri>
    <send_snail_mail>0</send_snail_mail>
    <send_email>1</send_email>
    <folder>active</folder>
    <lines>
      <line>
        <line_id>2</line_id>
        <name>Yard Work</name>
        <description>Mowed the lawn.</description>
        <unit_cost>10</unit_cost>
        <quantity>4</quantity>
        <amount>40</amount>
        <tax1_name>GST</tax1_name>
        <tax2_name>PST</tax2_name>
        <tax1_percent>5</tax1_percent>
        <tax2_percent>8</tax2_percent>
        <type>Item</type>
      </line>
    </lines>
  </recurring>

	val freshbooks = new RecurringInvoice(
		None, //invoiceId
		2,  //clientId
		None, //number
		Some(BigDecimal("40.68")), //amount
		None, //amountOutstanding
		None, //status
		Some(new LocalDate(2010, 10, 7)),//date
		Some("2314"), //poNumber
		Some(10), //discount
		Some("Due upon receipt."), //notes
		Some("Payment due in 30 days."),//terms
		Some("CAD"), //currencyCode
		Some("en"), //language
		None,//		_links:Option[Links], //Read-only
		Some(Url("http://example.com/account")), //_returnUri:Option[Url],
		None, //_updated:Option[DateTime], //Read-only
		Some(30), //_recurringId:Option[Long], //Read-only
		Some("John"), //_firstName:Option[String],
		Some("Smith"),//_lastName:Option[String],
		Some("ABC Corp"), //_organization:Option[String],
		Some(PrimaryAddress(Some("Street 1"), Some("Street 2"), Some("City"), Some("State"), Some("Country"), Some("Code"))), //_address:Option[PrimaryAddress],
		Some("Vat Name"), //_vatName:Option[String],
		Some(123456l), //_vatNumber:Option[Long], 
		Line(  //_lines:List[Line],
				Some(2),  //id
				Some(BigDecimal(40)),  //amount
				Some("Yard Work"), //Name
				Some("Mowed the lawn."),  //description
				BigDecimal("10"),  //unitcost
				4,//quantity
				Some("GST"),//taxSome(1
				Some("PST"),//tax2
				Some(5),//tax1Percent
				Some(8),//tax2Percent
				LineType.Item
			) :: Nil,
			0, //_occurences: Int,
			Monthly, //_frequency: Frequency,
		false, //_stopped: Boolean,
		true, //_sendEmail: Boolean,
		false, //_sendSnailMail: Boolean,
		None
	)
	val xml =  <recurring>
    <client_id>40</client_id>
    <date>2007-09-03</date>	
    <po_number>2314</po_number>	
    <discount>10</discount>	
    <notes>Due upon receipt.</notes>       
    <currency_code>CAD</currency_code> 
    <terms>Payment due in 30 days.</terms>
		<p_street1></p_street1>
		<p_street2></p_street2>
		<p_city></p_city>
		<p_state></p_state>
		<p_country></p_country>
		<p_code></p_code>
    <occurences>1</occurences>
    <frequency>monthly</frequency>
    <stopped>0</stopped>
    <send_email>1</send_email>
    <send_snail_mail>0</send_snail_mail>
    <autobill>                  
      <gateway_name>Authorize.net</gateway_name>  
      <card>
        <number>4111 1111 1111 1111</number>  
        <name>John Smith</name>
        <expiration>
          <month>3</month>
          <year>2012</year>
        </expiration>
      </card>
    </autobill>
    <lines>
      <line>
        <name>Yard Work</name>
        <description>Mowed the lawn.</description>
        <unit_cost>10</unit_cost>
        <quantity>4</quantity>
        <tax1_name>GST</tax1_name>
        <tax2_name>PST</tax2_name>
        <tax1_percent>5</tax1_percent>
        <tax2_percent>8</tax2_percent>
        <type>Time</type>                            
      </line>
    </lines>
  </recurring>


	val cardWithoutAutobill = new RecurringInvoice(
		None, //invoiceId
		40,  //clientId
		None, //number
		None, //amount
		None, //amountOutstanding
		None, //status
		Some(new LocalDate(2007, 9, 3)),//date
		Some("2314"), //poNumber
		Some(10), //discount
		Some("Due upon receipt."), //notes
		Some("Payment due in 30 days."),//terms
		Some("CAD"), //currencyCode
		None, //language
		None,//		_links:Option[Links], //Read-only
		None, //_returnUri:Option[Url],
		None, //_updated:Option[DateTime], //Read-only
		None, //_recurringId:Option[Long], //Read-only
		None, //_firstName:Option[String],
		None,//_lastName:Option[String],
		None, //_organization:Option[String],
		Some(PrimaryAddress(None, None, None, None, None, None)), //_address:Option[PrimaryAddress],
		None, //_vatName:Option[String],
		None, //_vatNumber:Option[Long], 
		Line(  //_lines:List[Line],
				None,
				None,
				Some("Yard Work"),
				Some("Mowed the lawn."),
				BigDecimal("10"),
				4,
				Some("GST"),
				Some("PST"),
				Some(5),
				Some(8),
				LineType.Time
			) :: Nil,
			1, //_occurences: Int,
			Monthly, //_frequency: Frequency,
		false, //_stopped: Boolean,
		true, //_sendEmail: Boolean,
		false, //_sendSnailMail: Boolean,
		None
	)

	val card = new RecurringInvoice(
		None, //invoiceId
		40,  //clientId
		None, //number
		None, //amount
		None, //amountOutstanding
		None, //status
		Some(new LocalDate(2007, 9, 3)),//date
		Some("2314"), //poNumber
		Some(10), //discount
		Some("Due upon receipt."), //notes
		Some("Payment due in 30 days."),//terms
		Some("CAD"), //currencyCode
		None, //language
		None,//		_links:Option[Links], //Read-only
		None, //_returnUri:Option[Url],
		None, //_updated:Option[DateTime], //Read-only
		None, //_recurringId:Option[Long], //Read-only
		None, //_firstName:Option[String],
		None,//_lastName:Option[String],
		None, //_organization:Option[String],
		Some(PrimaryAddress(None, None, None, None, None, None)), //_address:Option[PrimaryAddress],
		None, //_vatName:Option[String],
		None, //_vatNumber:Option[Long], 
		Line(  //_lines:List[Line],
				None,
				None,
				Some("Yard Work"),
				Some("Mowed the lawn."),
				BigDecimal("10"),
				4,
				Some("GST"),
				Some("PST"),
				Some(5),
				Some(8),
				LineType.Time
			) :: Nil,
			1, //_occurences: Int,
			Monthly, //_frequency: Frequency,
		false, //_stopped: Boolean,
		true, //_sendEmail: Boolean,
		false, //_sendSnailMail: Boolean,
		None //new Autobill
	)

	val xmlNoAutobill =  <recurring>
    <client_id>40</client_id>
    <date>2007-09-03</date>	
    <po_number>2314</po_number>	
    <discount>10</discount>	
    <notes>Due upon receipt.</notes>       
    <currency_code>CAD</currency_code> 
    <terms>Payment due in 30 days.</terms>
		<p_street1></p_street1>
		<p_street2></p_street2>
		<p_city></p_city>
		<p_state></p_state>
		<p_country></p_country>
		<p_code></p_code>
    <occurences>1</occurences>
    <frequency>monthly</frequency>
    <stopped>0</stopped>
    <send_email>1</send_email>
    <send_snail_mail>0</send_snail_mail>
    <lines>
      <line>
        <name>Yard Work</name>
        <description>Mowed the lawn.</description>
        <unit_cost>10</unit_cost>
        <quantity>4</quantity>
        <tax1_name>GST</tax1_name>
        <tax2_name>PST</tax2_name>
        <tax1_percent>5</tax1_percent>
        <tax2_percent>8</tax2_percent>
        <type>Time</type>                            
      </line>
    </lines>
  </recurring>
}

