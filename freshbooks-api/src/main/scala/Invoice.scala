package scala_mash.freshbooks_api.model

import xml._
import NodeSeq._
import org.joda.time.{LocalDate, DateTime}

import bizondemand.utils.models.internet.Url
import scala_mash.rest.util.Helpers._
import scala_mash.rest.{Ok,RestException}

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
		_invoiceId:Option[String],
		_clientId:String,
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
		val node : NodeSeq = xml \ ("invoice")
		new Invoice(
			Some(xml \ "invoice_id" text), 																								//invoiceId:Option[String],
			(xml \ "client_id" text), 																										//clientId:String,
			optionalString( node, "number"),																						 //number:Option[String],
			optionalBigDecimal(node, "amount"),  																					//amount:Option[BigDecimal]
			optionalBigDecimal(node, "amount_outstanding"),  															//amountOutstanding:Option[BigDecimal]
			InvoiceStatus.valueOf(node \ "status" text), 																	//status:InvoiceStatus,
			optionalYmd(node,"date"), 																										//date:Option[LocalDate],
			(if( ( node \ "po_number" text).isEmpty) None else Some(node \ "po_number" text)), //poNumber:Option[String],
			(if( ( node \ "discount" text).isEmpty) None else Some((node \ "discount" text).toInt)), //discount:Option[Int],
			(if( ( node \ "notes" text).isEmpty) None else Some(node \ "notes" text)), //notes:Option[String],
			(if( ( node \ "terms" text).isEmpty) None else Some(node \ "terms" text)), //terms:Option[String],
			(if( ( node \ "currency_code" text).isEmpty) None else Some(node \ "currency_code" text)), //currencyCode:Option[String],
			optionalString( node, "language"), //language:Option[String]
			Links.optionalParse( node),  //links:Option[Links]
			(if( ( node \ "return_uri" text).isEmpty) None else Some(Url(node \ "return_uri" text))), //returnUri:Option[URL],
			optionalDateTimeWithTimeZone(node,"updated"), //updated:Option[DateTime],
			optionalLong(node,"recurringId"), //recurringId:Option[Long],
			(if( ( node \ "first_name" text).isEmpty) None else Some(node \ "first_name" text)), //firstName:Option[String],
			(if( ( node \ "last_name" text).isEmpty) None else Some(node \ "last_name" text)), //lastName:Option[String],
			(if( ( node \ "organization" text).isEmpty) None else Some(node \ "organization" text)), //organization:Option[String],
			Some(PrimaryAddress(		//address:Option[PrimaryAddress],
				(if (( node \ "p_street1" text).isEmpty) None else Some((node \ "p_street1" text).toString)),
				(if (( node \ "p_street2" text).isEmpty) None else Some((node \ "p_street2" text).toString)),
				(if (( node \ "p_city" text).isEmpty) None else Some((node \ "p_city" text).toString)),
				(if (( node \ "p_state" text).isEmpty) None else Some((node \ "p_state" text).toString)),
				(if (( node \ "p_country" text).isEmpty) None else Some((node \ "p_country" text).toString)),
				(if (( node \ "p_code" text).isEmpty) None else Some((node \ "p_code" text).toString))
			)),
			optionalString(xml,"vat_name"),
			optionalLong(xml,"vat_number"),
			Line.parseList( node \\ "line") //lines:List[Line]
		)

	}

	def create( invoice:Invoice, account:Account) : Invoice = {
		debug("Invoice.create invoice: {}, account: {}",invoice, account)
		val request = <request method="invoice.create">{invoice.toXml}</request>
		val response = post(account.domainName, account.authenticationToken, request )
		debug("Invoice.create response: {}", response)
		response match {
			case n:Ok => {
				val invoiceId: Option[String] = parseCreateResponse(convertResponseToXml(n.response)) 
				new Invoice( 
					invoiceId,								//invoiceId:Option[String],
					invoice.clientId, 				//clientId:String,
					invoice.number, 					//number:Option[String],
					invoice.amount,
					invoice.amountOutstanding,
					invoice.status, //status:InvoiceStatus,
					invoice.date, //date:Option[LocalDate],
					invoice.poNumber, //poNumber:Option[String],
					invoice.discount, //discount:Option[Int],
					invoice.notes, //notes:Option[String],
					invoice.terms, //terms:Option[String],
					invoice.currencyCode, //currencyCode:Option[String],
					invoice.language,
					invoice.links,
					invoice.returnUri, //returnUri:Option[Url],
					invoice.updated,
					invoice.recurringId,
					invoice.firstName, //firstName:Option[String],
					invoice.lastName, //lastName:Option[String],
					invoice.organization, //organization:Option[String],
					invoice.address, //address:Option[PrimaryAddress],
					invoice.vatName,
					invoice.vatNumber,
					invoice.lines) //lines:List[Line])
			}
			case n => throw new RestException(n)
		}
	}

	def update( invoice: Invoice, account:Account) : Unit = {
		debug("Invoice.update invoice: {}, account {}", invoice, account)
		val request = <request method="invoice.update">{invoice.toXml}</request>
		val response = post(account.domainName, account.authenticationToken, request)
		debug("Invoice.update response: {}", response)
		response match {
			case n:Ok =>
			case n => throw new RestException(n)
		}
	}

	def get( invoiceId: Invoice, account:Account) :Invoice = {
		debug("Invoice.get invoiceId: {}, account {}", invoiceId, account)
		val request = <request method="invoice.get"><invoice_id>{invoiceId}</invoice_id></request>
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

