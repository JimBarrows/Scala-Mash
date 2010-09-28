package scala_mash.freshbooks_api.model

import xml._
import NodeSeq._
import org.joda.time.{LocalDate, DateTime}

import bizondemand.utils.models.internet.Url
import scala_mash.rest.util.Helpers._
import scala_mash.rest.{Ok,RestException}
import Utils._
import InvoiceStatus._
import LineType._
import Folder._

object Frequency extends Enumeration {
	type Frequency = Value
	val Weekly = Value("weekly")
	val Weeks2 = Value("2 weeks")
	val Weeks4 = Value("4 weeks")
	val Monthly = Value("monthly")
	val Months2 = Value("2 months")
	val Months3 = Value("3 months")
	val Months6 = Value("6 months")
	val Yearly = Value("yearly")
	val Years2 = Value("2 years")
}

import Frequency._


class RecurringInvoice(	_invoiceId:Option[Int],
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
		_lines:List[Line],
		_occurences: Int,
		_frequency: Frequency,
		_stopped: Boolean,
		_sendEmail: Boolean,
		_sendSnailMail: Boolean,
		_autobill: Option[Autobill]
	) extends Invoice (	_invoiceId:Option[Int],
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

		def occurences = _occurences
		def frequency = _frequency
		def stopped = _stopped
		def sendEmail = _sendEmail
		def sendSnailMail = _sendSnailMail
		def autobill = _autobill

	override def equals( that:Any):Boolean = that match {
			case dat:RecurringInvoice => super.equals( dat.asInstanceOf[Invoice]) &&
					dat.occurences == occurences &&
					dat.frequency == frequency &&
					dat.stopped == stopped &&
					dat.sendEmail == sendEmail &&
					dat.sendSnailMail == sendSnailMail &&
					dat.autobill == autobill 
			case _ => false
	}

	override def invoiceId_=( id:Int) : RecurringInvoice = {
		invoiceId match {
			case Some(x) => this
			case None => new RecurringInvoice( 
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
					this.lines,
					this.occurences,
					this.frequency,
					this.stopped,
					this.sendEmail,
					this.sendSnailMail,
					this.autobill)
		}
	}

	override def toXml : NodeSeq = {
		<recurring>
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
			<occurences>{occurences}</occurences>
			<frequency>{frequency}</frequency>
			<stopped>{if (stopped) 1 else 0}</stopped>
			<send_email>{if (sendEmail) 1 else 0}</send_email>
			<send_snail_mail>{if (sendSnailMail) 1 else 0}</send_snail_mail>
			{autobill.map( n=>n.toXml).getOrElse(Empty) }
			<lines>
				{lines.map( _.toXml)}
			</lines>
		</recurring>
	}

}


object RecurringInvoice extends FreshbooksResource[RecurringInvoice] {


def parseCreateResponse(xml:NodeSeq) = {
		debug("RecurringInvoice.parseCreateResponse xml: {}", xml)
		optionalString(xml, "invoice_id")
	}

	def parse( xml:NodeSeq) : Invoice = {
		debug("RecurringInvoice:parse xml {}", xml)
		new RecurringInvoice(
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
			Line.parseList( xml \\ "line"),
			(xml \ "occurences" text).toInt,
			Frequency.valueOf(xml \ "frequency" text).getOrElse(Monthly),
			boolean( xml, "stopped"),
			boolean( xml, "send_email"),
			boolean( xml, "send_snail_mail"),
			Autobill.optionalParse( xml)
		)

	}

	def parseList( node:NodeSeq) : List[Invoice] = {
		debug("Invoice.parseList {}", node)
		( node \\ "invoice").map( parse(_)).toList
	}

}


class Autobill( _gatewayName: String, _card: CreditCard) {

	def gatewayName = _gatewayName
	def card = _card

	def toXml = <autobill>
		<gateway_name>{gatewayName}</gateway_name>
		{card.toXml}
	</autobill>

	override def equals( that:Any):Boolean = that match {
			case dat:Autobill => dat.gatewayName == gatewayName &&
					dat.card == card
			case _ => false
	}
}


object Autobill {

	def parse( xml: NodeSeq) = new Autobill (
		xml \ "gateway_name" text,
		CreditCard.parse(xml \ "card")
	)

	def optionalParse(xml: NodeSeq) = xml match {
		case <autobill>_*</autobill> => Some( Autobill.parse( xml))
		case _ => None
	}
}


class CreditCard( _number:String, _name: String, _month: Int, _year: Int) {

	def number = _number
	def name = _name
	def month = _month
	def year = _year

	override def equals( that:Any):Boolean = that match {
			case dat:CreditCard => dat.number == number &&
					dat.name == name &&
					dat.month == month &&
					dat.year == year 
			case _ => false
	}

	def toXml = <card>
		<number>{number}</number>
		<name>{name}</name>
		<expiration>
			<month>{month}</month>
			<year>{year}</year>
		</expiration>
	</card>
}

object CreditCard {
	
	def parse( xml: NodeSeq) = new CreditCard(
		xml \ "number" text,
		xml \ "name" text,
		(xml \ "expiration" \ "month" text).toInt,
		(xml \"expiration" \ "year" text).toInt
	)
}
