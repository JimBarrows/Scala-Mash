package scala_mash.freshbooks_api.model

import xml._
import NodeSeq._
import org.joda.time.LocalDate

import bizondemand.utils.models.internet.Url
import scala_mash.freshbooks_api.Utils.parseDateTime
import scala_mash.rest.util.Helpers._
import scala_mash.rest.{Ok,RestException}

object InvoiceStatus extends Enumeration {
	type InvoiceStatus = Value
	val Sent = Value("sent")
	val Viewed = Value("viewed")
	val Paid = Value("paid")
	val Draft = Value("draft")

}

import InvoiceStatus._

case class Invoice(
		invoiceId:Option[String],
		clientId:String,
		number:Option[String],
		status:Option[InvoiceStatus],
		date:Option[LocalDate],
		poNumber:Option[String],
		discount:Option[Int],
		notes:Option[String],
		terms:Option[String],
		currencyCode:Option[String],
		returnUri:Option[Url],
		firstName:Option[String],
		lastName:Option[String],
		organization:Option[String],
		address:Option[PrimaryAddress],
		lines:List[Line]
	) {

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
			{terms.map( n => <terms>{n}</terms>).getOrElse(Empty)}
			{returnUri.map( n => <return_uri>{n}</return_uri>).getOrElse(Empty)}
			{firstName.map( n => <first_name>{n}</first_name>).getOrElse(Empty)}
			{lastName.map( n => <last_name>{n}</last_name>).getOrElse(Empty)}
			{organization.map( n => <organization>{n}</organization>).getOrElse(Empty)}
			{address.map( n => n.toXml).getOrElse(Empty)}
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
		Invoice(
			Some(xml \ "invoice_id" text), //invoiceId:Option[String],
			(xml \ "client_id" text), //clientId:String,
			(if( ( node \ "number" text).isEmpty) None else Some(node \ "number" text)), //number:Option[String],
			InvoiceStatus.valueOf(node \ "status" text), //status:InvoiceStatus,
			optionalYmd(node,"date"), //date:Option[LocalDate],
			(if( ( node \ "po_number" text).isEmpty) None else Some(node \ "po_number" text)), //poNumber:Option[String],
			(if( ( node \ "discount" text).isEmpty) None else Some((node \ "discount" text).toInt)), //discount:Option[Int],
			(if( ( node \ "notes" text).isEmpty) None else Some(node \ "notes" text)), //notes:Option[String],
			(if( ( node \ "terms" text).isEmpty) None else Some(node \ "terms" text)), //terms:Option[String],
			(if( ( node \ "currency_code" text).isEmpty) None else Some(node \ "currency_code" text)), //currencyCode:Option[String],
			(if( ( node \ "return_uri" text).isEmpty) None else Some(Url(node \ "return_uri" text))), //returnUri:Option[URL],
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
				Invoice( parseCreateResponse(convertResponseToXml(n.response)), //invoiceId:Option[String],
				invoice.clientId, //clientId:String,
				invoice.number, //number:Option[String],
				invoice.status, //status:InvoiceStatus,
				invoice.date, //date:Option[LocalDate],
				invoice.poNumber, //poNumber:Option[String],
				invoice.discount, //discount:Option[Int],
				invoice.notes, //notes:Option[String],
				invoice.terms, //terms:Option[String],
				invoice.currencyCode, //currencyCode:Option[String],
				invoice.returnUri, //returnUri:Option[Url],
				invoice.firstName, //firstName:Option[String],
				invoice.lastName, //lastName:Option[String],
				invoice.organization, //organization:Option[String],
				invoice.address, //address:Option[PrimaryAddress],
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
		debug("Invoice.get invoice: {}, account {}", invoice, account)
		val request = <request method="invoice.get"><invoice_id>{invoiceId}</invoice_id></request>
		val status = post( account.domainName, account, authenticationToken, request)
		debug("Invoice.get status: {}", status)
		status match {
			case n:Ok => parse( convertResponseToXml(n, response))
			case n => throw new RestException(
		}
	}
}




case class Line(
	name:Option[String],
	description:Option[String],
	unitCost:String,
	quantity:Int,
	tax1Name:Option[String],
	tax2Name:Option[String],
	tax1Percent:Option[Int],
	tax2Percent:Option[Int]){

	def toXml : NodeSeq = <line>
			{name.map( n => <name>{n}</name>).getOrElse(Empty)}
			{description.map( n => <description>{n}</description>).getOrElse(Empty)}
			<unit_cost>{unitCost}</unit_cost>
			<quantity>{quantity}</quantity>
			{tax1Name.map( n => <tax1_name>{n}</tax1_name>).getOrElse(Empty)}
			{tax2Name.map( n => <tax2_name>{n}</tax2_name>).getOrElse(Empty)}
			{tax1Percent.map( n => <tax1_percent>{n}</tax1_percent>).getOrElse(Empty)}
			{tax2Percent.map( n => <tax2_percent>{n}</tax2_percent>).getOrElse(Empty)}
		</line>
}

object Line {

	def parse( xml :NodeSeq) ={
		Line(
			( if ((xml \ "name" text).isEmpty) None else Some(xml \ "name" text)), 							//name:Option[String],
			( if ((xml \ "description" text).isEmpty) None else Some(xml \ "description" text)), //description:Option[String],
			xml \ "unit_cost" text, 				//unitCost:String,
			( xml\ "quantity" text).toInt, 						//quantity:Int,
			( if ((xml \ "tax1_name" text).isEmpty) None else Some( xml \ "tax1_name" text)), 				//tax1Name:Option[String],
			( if ((xml \ "tax2_name" text).isEmpty) None else Some( xml \ "tax2_name" text)), 				//tax2Name:Option[String],
			( if ((xml \ "tax1_percent" text).isEmpty) None else Some( (xml \ "tax1_percent" text).toInt)), 	//tax1Percent:Option[Int],
			( if ((xml \ "tax1_percent" text).isEmpty) None else Some( (xml \ "tax1_percent" text).toInt)) 		//tax2Percent:Option[Int]){
		)
	}

	def parseList( xml:NodeSeq) :List[Line] = {
		xml.map( parse(_)).toList
	}
}
