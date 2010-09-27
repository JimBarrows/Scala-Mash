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
	val Sent = Value("sent")
	val Viewed = Value("viewed")
	val Paid = Value("paid")
	val Draft = Value("draft")

}

object LineType extends Enumeration {
	type LineType = Value
	val Item = Value("Item")
	val Time = Value("Time")

}

import InvoiceStatus._
import LineType._

class Invoice(
		_invoiceId:Option[Int],
		_clientId:Int,
		_number:Option[String],
		_amount:Option[BigDecimal], //Read-only
		_amountOutstanding:Option[BigDecimal], //Read-only
		_status:Option[InvoiceStatus],
		_date:Option[LocalDate],
		_poNumber:Option[String],
		_discount:Option[Int],
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
					dat.amountOutstanding == amountOutstanding// &&
//					dat.status == status &&
//					dat.date == date &&
//					dat.poNumber == poNumber &&
//					dat.discount == discount &&
//					dat.notes == notes &&
//					dat.terms == terms &&
//					dat.currencyCode == currencyCode &&
//					dat.language == language &&
//					dat.links == links &&
//					dat.returnUri == returnUri &&
//					dat.updated == updated &&
//					dat.recurringId == recurringId &&
//					dat.firstName == firstName &&
//					dat.lastName == lastName &&
//					dat.organization == organization &&
//					dat.address == address &&
//					dat.vatName == vatName &&
//					dat.lines == lines 
			case _ => false
		}

	override def toString = "Invoice(invoiceId: %s, clientId: %d, number: %s, amount: %s, amountOutstanding: %s)".format(invoiceId, 
			clientId,
			number,amount, amountOutstanding)

	def invoiceId_=  (id :Int) : Invoice = {
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

	def parse( node:NodeSeq) : Invoice = {
		debug("Invoice:parse xml {}", node)
		val xml = node \"invoice"
		new Invoice(
			optionalInt(xml , "invoice_id"),
			(xml \ "client_id" text).toInt,
			optionalString( xml, "number"),
			optionalBigDecimal(xml, "amount"),
			optionalBigDecimal(xml, "amount_outstanding"),
			InvoiceStatus.valueOf(xml \ "status" text),
			optionalYmd(xml,"date"),
			optionalString(xml, "po_number"),
			optionalInt(xml, "discount"),
			optionalString(xml, "notes"),
			optionalString(xml, "terms"),
			optionalString(xml, "currency_code"),
			optionalString( xml, "language"), //language:Option[String]
			Links.optionalParse( xml),  //links:Option[Links]
			optionalUrl(xml, "return_uri"),
			optionalDateTime(xml,"updated"), //updated:Option[DateTime],
			optionalLong(xml,"recurringId"), //recurringId:Option[Long],
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

	def create( invoice:Invoice, account:Account) : Invoice = {
		debug("Invoice.create invoice: {}, account: {}",invoice, account)
		val request = <request method="invoice.create">{invoice.toXml}</request>
		val response = post(account.domainName, account.authenticationToken, request )
		debug("Invoice.create response: {}", response)
		response match {
			case n:Ok => {
				convertResponseToXml(n.response) match {
					case resp if (resp \ "@status" text) == "ok" => invoice.invoiceId = (resp \ "invoice_id" text).toInt
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

	def get( invoiceId: Int, account:Account) :Invoice = {
		debug("Invoice.get invoiceId: {}, account {}", invoiceId, account)
		val request = <request method="invoice.get"><invoice_id>{invoiceId}</invoice_id></request>
		println("request: " + request)
		val status = post( account.domainName, account.authenticationToken, request)
		debug("Invoice.get status: {}", status)
		status match {
			case n:Ok => parse( convertResponseToXml(n.response))
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
	quantity:Int,
	tax1Name:Option[String],
	tax2Name:Option[String],
	tax1Percent:Option[Int],
	tax2Percent:Option[Int],
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
			optionalLong(xml,"id"),
			optionalBigDecimal(xml,"amount"),
			optionalString(xml, "name"),
			optionalString(xml, "description"),
			BigDecimal( xml \ "unit_cost" text),
			(xml \ "quantity" text).toInt,
			optionalString(xml, "tax1_name"),
			optionalString(xml, "tax2_name"),
			optionalInt(xml, "tax1_percent"),
			optionalInt(xml, "tax2_percent"),
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

class FreshbooksApiException(msg:String) extends Exception(msg) {
}

class ExistingInvoiceNumberException extends FreshbooksApiException( "This invoice number is already in use. Please choose another.")  

class NoClientExistsException extends FreshbooksApiException( "Client does not exist.")
