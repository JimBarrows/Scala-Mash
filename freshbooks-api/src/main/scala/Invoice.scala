package scala_mash.freshbooks_api.model

import xml._
import NodeSeq._
import org.joda.time.{LocalDate, DateTime}

import bizondemand.utils.models.internet.Url
import scala_mash.rest.util.Helpers._
import scala_mash.rest.{Ok,RestException}
import Utils._

object InvoiceStatus extends Enumeration {
	type InvoiceStatus = Value
	val Disputed = Value("disputed")
	val Draft = Value("draft")
	val Sent = Value("sent")
	val Viewed = Value("viewed")
	val Paid = Value("paid")
	val AutoPaid = Value("auto-paid")
	val Retry = Value("retry")
	val Failed = Value("failed")
	val Unpaid = Value("unpaid")  //This will return all invoices in disputed, sent, viewed, retry or failed

}

object LineType extends Enumeration {
	type LineType = Value
	val Item = Value("Item")
	val Time = Value("Time")

}

object Folder extends Enumeration {
	type Folder = Value
	val Active = Value("active")
	val Archived = Value("archived")
	val Deleted = Value("deleted")
}

import InvoiceStatus._
import LineType._
import Folder._

class Invoice(
		_invoiceId:Option[Long],
		_clientId:Long,
		_number:Option[String],
		_amount:Option[BigDecimal], //Read-only
		_amountOutstanding:Option[BigDecimal], //Read-only
		_status:Option[InvoiceStatus],
		_date:Option[LocalDate],
		_poNumber:Option[String],
		_discount:Option[Long],
		_notes:Option[String],
		_terms:Option[String],
		_currencyCode:Option[String],
		_language:Option[String],
		_links:Option[Links], //Read-only
		_returnUri:Option[Url],
		_updated:Option[DateTime], //Read-only
		_recurringId:Option[Long], //Read-only
		_firstName:Option[String],
		_lastName:Option[String],
		_organization:Option[String],
		_address:Option[PrimaryAddress],
		_vatName:Option[String],
		_vatNumber:Option[Long], 
		_lines:List[Line]
	) {

	def invoiceId  = _invoiceId
	def clientId  = _clientId
	def amount  = _amount
	def amountOutstanding  = _amountOutstanding
	def number = _number
	def status  = _status
	def date  = _date
	def poNumber  = _poNumber
	def discount  = _discount
	def notes  = _notes
	def terms  = _terms
	def currencyCode  = _currencyCode
	def language  = _language
	def links  = _links
	def returnUri  = _returnUri
	def updated  = _updated
	def recurringId  = _recurringId
	def firstName  = _firstName
	def lastName  = _lastName
	def organization= _organization
	def address = _address
	def vatNumber = _vatNumber
	def vatName = _vatName
	def lines = _lines 

	override def equals( that:Any):Boolean = that match {
			case dat:Invoice => dat.invoiceId == invoiceId && 
					dat.clientId == clientId && 
					dat.number == number &&
					dat.amount == amount &&
					dat.amountOutstanding == amountOutstanding &&
					dat.status == status &&
					dat.date == date &&
					dat.poNumber == poNumber &&
					dat.discount == discount &&
					dat.notes == notes &&
					dat.terms == terms &&
					dat.currencyCode == currencyCode &&
					dat.language == language &&
					dat.links == links &&
					dat.returnUri == returnUri &&
					dat.updated == updated &&
					dat.recurringId == recurringId &&
					dat.firstName == firstName &&
					dat.lastName == lastName &&
					dat.organization == organization &&
					dat.address == address &&
					dat.vatName == vatName &&
					dat.vatNumber == vatNumber &&
					dat.lines == lines 
			case _ => false
		}

	override def toString = "Invoice(invoiceId: %s, clientId: %d, number: %s, amount: %s, amountOutstanding: %s, status: %s, date: %s, poNumber: %s, discount: %s, notes: %s, terms: %s, currencyCode: %s, language: %s, links: %s, returnUri: %s, update: %s, recurringId: %s, firstName: %s, lastName %s, organization: %s, address: %s, vatName: %s, vatNumber: %s, lines: %s)".format(invoiceId, 
			clientId,
			number,
			amount, 
			amountOutstanding,
			status,
			date.map(d => printYmd(d)).getOrElse("None"), 
			poNumber,
			discount,
			notes,
			terms,
			currencyCode,
			language,
			links,
			returnUri,
			updated.map(d => printDateTime(d)).getOrElse("None"),
			recurringId,
			firstName,
			lastName,
			organization,
			address,
			vatName,
			vatNumber,
			lines
			)

	def invoiceId_=  (id :Long) : Invoice = {
		invoiceId match {
			case Some(x) => this
			case None => new Invoice( 
					Some(id),
					this.clientId,
					this.number,
					this.amount,
					this.amountOutstanding,
					this.status,
					this.date,
					this.poNumber,
					this.discount,
					this.notes,
					this.terms,
					this.currencyCode,
					this.language,
					this.links,
					this.returnUri,
					this.updated,
					this.recurringId,
					this.firstName,
					this.lastName,
					this.organization,
					this.address,
					this.vatName,
					this.vatNumber,
					this.lines)
		}

	}

	def toXml : NodeSeq = {
		<invoice>
			{invoiceId.map( id => <invoice_id>{ id }</invoice_id>).getOrElse(Empty)}
			<client_id>{clientId}</client_id>
			{number.map( n => <number>{n}</number>).getOrElse(Empty)}
			{status.map( s => <status>{s}</status>).getOrElse(Empty)}
			{date.map( d => <date>{printYmd(d)}</date>).getOrElse(Empty)}
			{poNumber.map( n => <po_number>{n}</po_number>).getOrElse(Empty)}
			{discount.map( n => <discount>{n}</discount>).getOrElse(Empty)}
			{notes.map( n => <notes>{n}</notes>).getOrElse(Empty)}
			{currencyCode.map( n => <currency_code>{n}</currency_code>).getOrElse(Empty)}
			{language.map( n => <language>{n}</language>).getOrElse(Empty)}
			{terms.map( n => <terms>{n}</terms>).getOrElse(Empty)}
			{returnUri.map( n => <return_uri>{n}</return_uri>).getOrElse(Empty)}
			{updated.map( n => <updated>{printDateTime( n)}</updated>).getOrElse(Empty)}
			{firstName.map( n => <first_name>{n}</first_name>).getOrElse(Empty)}
			{lastName.map( n => <last_name>{n}</last_name>).getOrElse(Empty)}
			{organization.map( n => <organization>{n}</organization>).getOrElse(Empty)}
			{address.map( n => n.toXml).getOrElse(Empty)}
			{vatName.map( n => <vat_name>{n}</vat_name>).getOrElse(Empty)}
			{vatNumber.map( n => <vat_number>{n}</vat_number>).getOrElse(Empty)}
			<lines>
				{lines.map( _.toXml)}
			</lines>
		</invoice>
	}
}

object Invoice extends FreshbooksResource[Invoice] {

	def parseCreateResponse(xml:NodeSeq) = {
		debug("Invoice.parseCreateResponse xml: {}", xml)
		optionalString(xml, "invoice_id")
	}

	def parse( xml:NodeSeq) : Invoice = {
		debug("Invoice:parse xml {}", xml)
		new Invoice(
			optionalLong(xml , "invoice_id"),
			(xml \ "client_id" text).toLong,
			optionalString( xml, "number"),
			optionalBigDecimal(xml, "amount"),
			optionalBigDecimal(xml, "amount_outstanding"),
			InvoiceStatus.valueOf(xml \ "status" text),
			optionalYmd(xml,"date"),
			optionalString(xml, "po_number"),
			optionalLong(xml, "discount"),
			optionalString(xml, "notes"),
			optionalString(xml, "terms"),
			optionalString(xml, "currency_code"),
			optionalString( xml, "language"), //language:Option[String]
			Links.optionalParse( xml),  //links:Option[Links]
			optionalUrl(xml, "return_uri"),
			optionalDateTime(xml,"updated"), //updated:Option[DateTime],
			optionalLong(xml,"recurring_id"), //recurringId:Option[Long],
			optionalString(xml, "first_name"),
			optionalString(xml, "last_name"),
			optionalString(xml, "organization"),
			Some(PrimaryAddress(		//address:Option[PrimaryAddress],
				optionalString(xml, "p_street1"),
				optionalString(xml, "p_street2"),
				optionalString(xml, "p_city"),
				optionalString(xml, "p_state"),
				optionalString(xml, "p_country"),
				optionalString(xml, "p_code")
			)),
			optionalString(xml,"vat_name"),
			optionalLong(xml,"vat_number"),
			Line.parseList( xml \\ "line") //lines:List[Line]
		)

	}

	def parseList( node:NodeSeq) : List[Invoice] = {
		debug("Invoice.parseList {}", node)
		( node \\ "invoice").map( parse(_)).toList
	}

	def create( invoice:Invoice, account:Account) : Invoice = {
		debug("Invoice.create invoice: {}, account: {}",invoice, account)
		val request = <request method="invoice.create">{invoice.toXml}</request>
		val response = post(account.domainName, account.authenticationToken, request )
		debug("Invoice.create response: {}", response)
		response match {
			case n:Ok => {
				convertResponseToXml(n.response) match {
					case resp if (resp \ "@status" text) == "ok" => invoice.invoiceId = (resp \ "invoice_id" text).toLong
					case resp if( resp \ "error" text) =="This invoice number is already in use. Please choose another." => 
						throw new ExistingInvoiceNumberException
					case resp if( resp \ "error" text) =="Client does not exist." => 
						throw new NoClientExistsException
					case resp if( resp \ "@status" text) == "fail" => throw new FreshbooksApiException( resp \ "error" text)
				}
			}
			case n => throw new RestException(n)
		}
	}

	def update( invoice: Invoice, account:Account) : Invoice = {
		debug("Invoice.update invoice: {}, account {}", invoice, account)
		val request = <request method="invoice.update">{invoice.toXml}</request>
		val response = post(account.domainName, account.authenticationToken, request)
		debug("Invoice.update response: {}", response)
		response match {
			case n:Ok => invoice
			case n => throw new RestException(n)
		}
	}

	def get( invoiceId: Long, account:Account) :Invoice = {
		debug("Invoice.get invoiceId: {}, account {}", invoiceId, account)
		val request = <request method="invoice.get"><invoice_id>{invoiceId}</invoice_id></request>
		val status = post( account.domainName, account.authenticationToken, request)
		status match {
			case n:Ok => parse( convertResponseToXml(n.response) \ "invoice")
			case n => throw new RestException(n)
		}
	}

	def list( clientId: Option[Long], 
			recurringId: Option[Long], 
			status: Option[InvoiceStatus], 
			dateFrom: Option[LocalDate], 
			dateTo: Option[LocalDate], 
			updatedFrom: Option[LocalDate], 
			updatedTo: Option[LocalDate], 
			page: Option[Long],
			perPage: Option[Long],
			folder: Option[Folder], account:Account): InvoiceList = {

		val request= <request method="invoice.list">
			{clientId.map( n => <client_id>{n}</client_id>).getOrElse(Empty)}
			{recurringId.map( n => <recurring_id>{n}</recurring_id>).getOrElse(Empty)}
			{status.map( n => <status>{n}</status>).getOrElse(Empty)}
			{dateFrom.map( n => <date_from>{printYmd(n)}</date_from>).getOrElse(Empty)}
			{dateTo.map( n => <date_to>{printYmd(n)}</date_to>).getOrElse(Empty)}
			{updatedFrom.map( n => <updated_from>{printYmd(n)}</updated_from>).getOrElse(Empty)}
			{updatedTo.map( n => <updated_to>{printYmd(n)}</updated_to>).getOrElse(Empty)}
			{page.map( n => <page>{n}</page>).getOrElse(Empty)}
			{perPage.map( n => <per_page>{n}</per_page>).getOrElse(Empty)}
			{folder.map( n => <folder>{n}</folder>).getOrElse(Empty)}
		</request>

		val returnStatus = post( account.domainName, account.authenticationToken, request)

		returnStatus match {
			case n:Ok => InvoiceList.parse( convertResponseToXml(n.response))
			case n => throw new RestException(n)
		}
	}

	def delete( invoiceId: Long, account: Account) = {
		val request = <request method="invoice.delete"><invoice_id>{invoiceId}</invoice_id></request>

		val returnStatus = post( account.domainName, account.authenticationToken, request)

		returnStatus match {
			case n:Ok => 
			case n => throw new RestException(n)
		}
	}
}

case class Line(
	id:Option[Long], //read-only
	amount:Option[BigDecimal], //read-only
	name:Option[String],
	description:Option[String],
	unitCost:BigDecimal,
	quantity:Long,
	tax1Name:Option[String],
	tax2Name:Option[String],
	tax1Percent:Option[Long],
	tax2Percent:Option[Long],
	lineType:LineType){

	def toXml : NodeSeq = <line>
			{name.map( n => <name>{n}</name>).getOrElse(Empty)}
			{description.map( n => <description>{n}</description>).getOrElse(Empty)}
			<unit_cost>{unitCost}</unit_cost>
			<quantity>{quantity}</quantity>
			{tax1Name.map( n => <tax1_name>{n}</tax1_name>).getOrElse(Empty)}
			{tax2Name.map( n => <tax2_name>{n}</tax2_name>).getOrElse(Empty)}
			{tax1Percent.map( n => <tax1_percent>{n}</tax1_percent>).getOrElse(Empty)}
			{tax2Percent.map( n => <tax2_percent>{n}</tax2_percent>).getOrElse(Empty)}
			<type>{lineType}</type>
		</line>
}

object Line {

	def parse( xml :NodeSeq) ={
		Line(
			optionalLong(xml,"line_id"),
			optionalBigDecimal(xml,"amount"),
			optionalString(xml, "name"),
			optionalString(xml, "description"),
			BigDecimal( xml \ "unit_cost" text),
			(xml \ "quantity" text).toLong,
			optionalString(xml, "tax1_name"),
			optionalString(xml, "tax2_name"),
			optionalLong(xml, "tax1_percent"),
			optionalLong(xml, "tax2_percent"),
			LineType.valueOf( xml \ "type" text).getOrElse(Item)
		)
	}

	def parseList( xml:NodeSeq) :List[Line] = {
		xml.map( parse(_)).toList
	}
}



case class Links ( clientView:Option[Url],
										view:Option[Url],
										edit:Option[Url]) {
	def toXml : NodeSeq = <links>
		<client_view>{clientView}</client_view>
		<view>{view}</view>
		<edit>{edit}</edit>
	</links>
}

object Links {
	def parse(xml:NodeSeq) = {
		Links(
			optionalUrl(xml, "client_view"),
			optionalUrl(xml,"view"),
			optionalUrl(xml,"edit")
		)
	}

	def optionalParse(xml:NodeSeq) = {
		val node = xml \ "links"
		if( node.isEmpty) None
		else Some(parse( node))
	}
}

class InvoiceList( _page: Long, _perPage: Long, _pages: Long, _total: Long, _invoices: List[Invoice]) {

	def page = _page
	def perPage = _perPage
	def pages = _pages
	def total = _total
	def invoices = _invoices

	override def equals( that:Any):Boolean = that match {
			case dat:InvoiceList => dat.page == page && 
					dat.perPage == perPage && 
					dat.pages == pages && 
					dat.total == total && 
					dat.invoices == invoices  
			case _ => false
			}
}

object InvoiceList {

	def parse( xml: NodeSeq) = {
		new InvoiceList(
			(xml \ "invoices" \ "@page" text).toLong,
			(xml \ "invoices" \ "@per_page" text).toLong,
			(xml \ "invoices" \ "@pages" text).toLong,
			(xml \ "invoices" \ "@total" text).toLong,
			Invoice.parseList(xml \"invoices")
		)
	}
}
