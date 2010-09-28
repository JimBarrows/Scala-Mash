package specs

import org.specs.Specification

import bizondemand.utils.models.internet.Url

import scala_mash.freshbooks_api._
import model._
import InvoiceStatus._

import scala_mash.freshbooks_api.Utils._
import scala_mash.rest.util.Helpers._
import scala_mash.rest.RestException

import _root_.specs.Utils._

object InvoiceServicesSpec extends Specification {

	"Invoice Services" should {

		shareVariables()
		setSequential()

		var currentInvoice :Invoice = invoice 

		"create invoices" in {
			currentInvoice = Invoice.create(invoice, account);
			currentInvoice.invoiceId must beSome[Int]
		}

		"when creating an invoice with existing invoice number, throw an ExistingInvoiceNumberException" in {
			Invoice.create(invoiceExistingNumber, account) must throwA[ExistingInvoiceNumberException]
		}

		"when creating an invoice without an existing client, throw an NoClientExistsException" in {
			Invoice.create(invoiceNoCustomer, account) must throwA[NoClientExistsException]
		}
		
		"update invoices" in {
			Invoice.update( currentInvoice, account) must be_== (currentInvoice)
		}

		"get invoices" in {
			val gotten = Invoice.get( currentInvoice.invoiceId.getOrElse(0), account);
			gotten.invoiceId must beEqualTo( currentInvoice.invoiceId)
			currentInvoice = gotten
		}

		"list invoices" in {
			val gotten = Invoice.list( None, None, None, None, None, None,None,None,None,None, account)
			gotten.invoices must contain( currentInvoice)
		}
		
		"delete invoices" in {
			Invoice.delete( currentInvoice.invoiceId.getOrElse(0), account) 
			currentInvoice.invoiceId must beSome[Int]
		}

	}


	val invoice = new Invoice(
			None, 		// invoiceId:Option[String],
			2, 					// clientId:String,
			None, // number:Option[String],
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

	val invoiceExistingNumber = new Invoice(
			None, 		// invoiceId:Option[String],
			2, 					// clientId:String,
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

	val invoiceNoCustomer = new Invoice(
			None, 		// invoiceId:Option[String],
			1, 					// clientId:String,
			None, // number:Option[String],
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
}
