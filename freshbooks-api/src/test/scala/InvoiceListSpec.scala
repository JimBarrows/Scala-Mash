package specs

import org.specs.Specification
import org.specs.runner.JUnit4

import org.joda.time.DateTime

import scala.{List, Nil}

import bizondemand.utils.models.internet.Url
import scala_mash.freshbooks_api.model._
import InvoiceStatus._
import scala_mash.freshbooks_api.Utils._
import scala_mash.rest.util.Helpers._

object InvoiceListSpec extends Specification {

  "The InvoiceList class"  should {
  	
  	"be able to parse the response" in {
  		InvoiceList.parse( responseXml) must be_==( response)
  	}
  }



	val invoice = new Invoice(
			Some(13), 		// invoiceId:Option[String],
			13, 					// clientId:String,
			Some("FB00004"), // number:Option[String],
			None, //amount
			None, //amountOustanding
			Some(Draft), // status:InvoiceStatus,
			Some(parseYmd("2007-06-23").toLocalDate), // date:Option[LocalDate],
			Some("2314"), // poNumber:Option[String],
			Some(10), // discount:Option[Int],
			Some("Due upon receipt."), // notes:Option[String],
			Some("Payment due in 30 days."), // terms:Option[String],
			Some("CAD"), // currencyCode:Option[String],
			Some("en"),  //language
			None, // links
			Some(Url("http://example.com/account")), // returnUri:Option[URL],
			None,  //updated
			None,  //recurringId
			Some("John"), // firstName:Option[String],
			Some("Smith"), // lastName:Option[String],
			Some("ABC Corp"), // organization:Option[String],
			Some( PrimaryAddress(		// address:Option[PrimaryAddress],
					Some("Street 1"), 
					Some("Street 2"), 
					Some("City"), 
					Some("State"), 
					Some("Country"), 
					Some("Code"))), 
			Some("Vat Name"), //vatName
			Some(123456), //vatNumber
			Line(  // lines:Option[List[Line]]
					None,
					None,
					Some("Yard Work"), // name:Option[String],
					Some("Mowed the lawn."), // description:Option[String],
					BigDecimal("10"), // unitCost:String,
					4, // quantity:Int,
					Some("GST"), // tax1Name:Option[String],
					Some("PST"), // tax2Name:Option[String],
					Some(5), // tax1Percent:Option[Int],
					Some(8), // tax2Percent:Option[Int]
					LineType.Item //lineType:LineType
			) :: Nil
	)

	val response = new InvoiceList(1, 10, 4, 47, invoice :: Nil)

	val responseXml = <response xmlns="http://www.freshbooks.com/api/" status="ok">
		<invoices page="1" per_page="10" pages="4" total="47">
			<invoice>
				<invoice_id>13</invoice_id>
				<client_id>13</client_id>  
				<number>FB00004</number>   
				<status>draft</status>    
				<date>2007-06-23</date>	      
				<po_number>2314</po_number>			  
				<discount>10</discount>	          
				<notes>Due upon receipt.</notes>	
				<currency_code>CAD</currency_code>
				<language>en</language>
				<terms>Payment due in 30 days.</terms>
				<return_uri>http://example.com/account</return_uri>
				<first_name>John</first_name> 
				<last_name>Smith</last_name>               
				<organization>ABC Corp</organization>     
				<p_street1>Street 1</p_street1>                  
				<p_street2>Street 2</p_street2>                 
				<p_city>City</p_city>                      
				<p_state>State</p_state>                   
				<p_country>Country</p_country>              
				<p_code>Code</p_code>                   
				<vat_name>Vat Name</vat_name>    
				<vat_number>123456</vat_number>
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
						<type>Item</type>                   
					</line>
				</lines>
			</invoice>
		</invoices>
	</response>


  val requestXml = <request method="invoice.list">
  <client_id>3</client_id>       
  <recurring_id>10</recurring_id>     
  <status>draft</status>              
  <date_from>2007-01-01</date_from>   
  <date_to>2007-04-01</date_to>
  <updated_from>2007-01-01 00:00:00</updated_from>
  <updated_to>2007-01-02 00:00:00</updated_to>
  <page>1</page>                    
  <per_page>10</per_page>
  <folder>active</folder>
</request>
}
