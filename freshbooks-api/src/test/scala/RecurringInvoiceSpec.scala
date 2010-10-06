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

		"be able to generate XML " in {
			card.toXml must ==/ ( xml)
		}

		"be able to parse XML " in {
			val parsedCard = RecurringInvoice.parse( xml) 
			println(parsedCard.address)
			println(card.address)
			parsedCard must be_==( card)
		}
	}

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
		new Autobill("Authorize.net", 
				new CreditCard("4111 1111 1111 1111","John Smith",3, 2012))
	)
}

