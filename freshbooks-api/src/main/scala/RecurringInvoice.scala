package scala_mash.freshbooks_api.model

import xml._
import NodeSeq._
import org.joda.time.{LocalDate, DateTime}

import bizondemand.utils.models.internet.Url
import bizondemand.utils.logging.Log
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


class RecurringInvoice(	_invoiceId:Option[Long],
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
		_lines:List[Line],
		_occurrences: Long,
		_frequency: Frequency,
		_stopped: Boolean,
		_sendEmail: Boolean,
		_sendSnailMail: Boolean,
		_autobill: Option[Autobill]
	) extends Invoice (	_invoiceId:Option[Long],
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

		def occurrences = _occurrences
		def frequency = _frequency
		def stopped = _stopped
		def sendEmail = _sendEmail
		def sendSnailMail = _sendSnailMail
		def autobill = _autobill

	override def equals( that:Any):Boolean = that match {
			case dat:RecurringInvoice => super.equals( dat.asInstanceOf[Invoice]) &&
					dat.occurrences == occurrences &&
					dat.frequency == frequency &&
					dat.stopped == stopped &&
					dat.sendEmail == sendEmail &&
					dat.sendSnailMail == sendSnailMail &&
					dat.autobill == autobill 
			case _ => false
	}

	override def toString = "RecurringInvoice(  %s, occurrences: %d, frequency: %s, stopped: %B, sendEmail: %B, sendSnailMail: %B, autobill: %s".format(super.toString, occurrences, frequency, stopped, sendEmail, sendSnailMail, autobill.toString)

	def recurringId_=( id: Long): RecurringInvoice = {
		recurringId match {
			case Some(x) => this
			case None => new RecurringInvoice( 
					this.invoiceId,
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
					Some(id),
					this.firstName,
					this.lastName,
					this.organization,
					this.address,
					this.vatName,
					this.vatNumber,
					this.lines,
					this.occurrences,
					this.frequency,
					this.stopped,
					this.sendEmail,
					this.sendSnailMail,
					this.autobill)
		}
	}

	override def invoiceId_=( id:Long) : RecurringInvoice = {
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
					this.occurrences,
					this.frequency,
					this.stopped,
					this.sendEmail,
					this.sendSnailMail,
					this.autobill)
		}
	}

	override def toXml : NodeSeq = 
		<recurring>
			{/*invoiceId.map( id => <invoice_id>{ id }</invoice_id>).getOrElse(Empty)*/}
			<client_id>{clientId}</client_id>
			{/*number.map( n => <number>{n}</number>).getOrElse(Empty)*/}
			{/*status.map( s => <status>{s}</status>).getOrElse(Empty)*/}
			{date.map( d => <date>{printYmd(d)}</date>).getOrElse(Empty)}
			{poNumber.map( n => <po_number>{n}</po_number>).getOrElse(Empty)}
			{discount.map( n => <discount>{n}</discount>).getOrElse(Empty)}
			{notes.map( n => <notes>{n}</notes>).getOrElse(Empty)}
			{currencyCode.map( n => <currency_code>{n}</currency_code>).getOrElse(Empty)}
			{language.map( n => <language>{n}</language>).getOrElse(Empty)}
			{terms.map( n => <terms>{n}</terms>).getOrElse(Empty)}
			{returnUri.map( n => <return_uri>{n}</return_uri>).getOrElse(Empty)}
			{updated.map( n => <updated>{printDateTime( n)}</updated>).getOrElse(Empty)}
			{recurringId.map( n => <recurring_id>{n}</recurring_id>).getOrElse(Empty)}
			{firstName.map( n => <first_name>{n}</first_name>).getOrElse(Empty)}
			{lastName.map( n => <last_name>{n}</last_name>).getOrElse(Empty)}
			{organization.map( n => <organization>{n}</organization>).getOrElse(Empty)}
			{address.map( n => n.toXml).getOrElse(Empty)}
			{vatName.map( n => <vat_name>{n}</vat_name>).getOrElse(Empty)}
			{vatNumber.map( n => <vat_number>{n}</vat_number>).getOrElse(Empty)}
			<occurrences>{occurrences}</occurrences>
			<frequency>{frequency}</frequency>
			<stopped>{if (stopped) 1 else 0}</stopped>
			<send_email>{if (sendEmail) 1 else 0}</send_email>
			<send_snail_mail>{if (sendSnailMail) 1 else 0}</send_snail_mail>
			{autobill.map( n => n.toXml).getOrElse(Empty)}
			<lines>
				{lines.map( _.toXml)}
			</lines>
		</recurring>
	

}


object RecurringInvoice extends FreshbooksResource[RecurringInvoice] {


	def parseCreateResponse(xml:NodeSeq) = {
		debug("Recurringrecurring.parseCreateResponse xml: {}", xml)
		optionalString(xml, "invoice_id")
	}

	def parse( xml:NodeSeq): RecurringInvoice = {
		debug("RecurringInvoice:parse xml {}", xml)
		new RecurringInvoice(
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
			Line.parseList( xml \\ "line"),
			(xml\"occurrences" text).toLong,
			Frequency.valueOf(xml \ "frequency" text).getOrElse(Monthly),
			boolean( xml, "stopped"),
			boolean( xml, "send_email"),
			boolean( xml, "send_snail_mail"),
			Autobill.optionalParse( xml \ "autobill" )
		)

	}

	def parseList( node:NodeSeq) : List[RecurringInvoice] = {
		debug("recurring.parseList {}", node)
		( node \\ "recurring").map( parse(_)).toList
	}

	def create( invoice: RecurringInvoice, account:Account) : RecurringInvoice = {
		debug("RecurringInvoice.create invoice: {}, account: {}",invoice, account)
		val request = <request method="recurring.create">{invoice.toXml}</request>
		val response = post(account.domainName, account.authenticationToken, request )
		debug("RecurringInvoice.create response: {}", response)
		response match {
			case n:Ok => {
				convertResponseToXml(n.response) match {
					case resp if (resp \ "@status" text) == "ok" => {
						invoice.invoiceId_=((resp \ "invoice_id" text).toLong).recurringId = (resp \ "recurring_id" text).toLong
					}
					case resp if( resp \ "error" text) =="This invoice number is already in use. Please choose another." => 
						throw new ExistingInvoiceNumberException
					case resp if( resp \ "error" text) =="Client does not exist." => 
						throw new NoClientExistsException
					case resp if( resp \ "error" text) =="Auto-billing is only available in the base currency." => 
						throw new AutobillingOnlyAvailableInBaseCurrency
					case resp if( resp \ "@status" text) == "fail" => 
						throw new FreshbooksApiException( resp \ "error" text)
				}
			}
			case n => throw new RestException(n)
		}
	}

	def update( invoice: RecurringInvoice, account:Account) : RecurringInvoice = {
		debug("RecurringInvoice.update invoice: {}, account {}", invoice, account)
		val request = <request method="recurring.update">{invoice.toXml}</request>
		val response = post(account.domainName, account.authenticationToken, request)
		debug("RecurringInvoice.update response: {}", response)
		response match {
			case n:Ok => invoice
			case n => throw new RestException(n)
		}
	}

	def get( recurringId: Long, account:Account): RecurringInvoice = {
		debug("RecurringInvoice.get recurringId: {}, account {}", recurringId, account)
		val request = <request method="recurring.get"><recurring_id>{recurringId}</recurring_id></request>
		val status = post( account.domainName, account.authenticationToken, request)
		debug("RecurringInvoice.get response: {}", status)
		status match {
			case n:Ok => parse( convertResponseToXml(n.response) \ "recurring")
			case n => throw new RestException(n)
		}
	}

	def list( clientId: Option[Long], 
			autobill:Option[Autobill],
			page: Option[Long],
			perPage: Option[Long],
			folder: Option[Folder], account:Account): RecurringInvoiceList = {

		val request= <request method="recurring.list">
			{clientId.map( n => <client_id>{n}</client_id>).getOrElse(Empty)}
			{autobill.map( n => n.toXml)}
			{page.map( n => <page>{n}</page>).getOrElse(Empty)}
			{perPage.map( n => <per_page>{n}</per_page>).getOrElse(Empty)}
			{folder.map( n => <folder>{n}</folder>).getOrElse(Empty)}
		</request>

		val response = post( account.domainName, account.authenticationToken, request)

		debug("RecurringInvoice.get list: {}", response)
		response match {
			case n:Ok => RecurringInvoiceList.parse( convertResponseToXml(n.response))
			case n => throw new RestException(n)
		}
	}

	def delete( recurringId: Long, account: Account) = {
		val request = <request method="recurring.delete"><recurring_id>{recurringId}</recurring_id></request>

		val returnStatus = post( account.domainName, account.authenticationToken, request)

		debug("RecurringInvoice.delete response: {}", returnStatus)

		returnStatus match {
			case n:Ok => 
			case n => throw new RestException(n)
		}
	}
}

class RecurringInvoiceList( _page: Long, _perPage: Long, _pages: Long, _total: Long, _invoices: List[RecurringInvoice]) {

	def page = _page
	def perPage = _perPage
	def pages = _pages
	def total = _total
	def invoices = _invoices

	override def equals( that:Any):Boolean = that match {
			case dat:RecurringInvoiceList => dat.page == page && 
					dat.perPage == perPage && 
					dat.pages == pages && 
					dat.total == total && 
					dat.invoices == invoices  
			case _ => false
			}

	override def toString = "RecurringInvoiceList( page: %d, perPage: %d, pages: %d, total: %d, invoices: %s".format(page, perPage, pages, total, invoices)
}

object RecurringInvoiceList {

	def parse( xml: NodeSeq) = {
		new RecurringInvoiceList(
			(xml \ "recurrings" \ "@page" text).toLong,
			(xml \ "recurrings" \ "@per_page" text).toLong,
			(xml \ "recurrings" \ "@pages" text).toLong,
			(xml \ "recurrings" \ "@total" text).toLong,
			RecurringInvoice.parseList(xml \\ "recurrings")
		)
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

	override def toString="Autobill( gatewayName: %s, %s)".format( gatewayName, card)
}


object Autobill extends Log{

	def parse( xml: NodeSeq) = {
		debug("Autobill.parse( xml: %s".format(xml))
		new Autobill (
			xml \ "gateway_name" text,
			CreditCard.parse(xml \ "card")
		)
	}

	def optionalParse( xml: NodeSeq): Option[Autobill] = {
		debug("Autobill.optionalParse( xml: %s".format(xml))
		xml match {
			case <autobill>{_*}</autobill> => Some(Autobill.parse(xml))
			case _ => None
		}
	}
}


class CreditCard( _number:String, _name: String, _month: Long, _year: Long) {

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

	override def toString="CreditCard( number: %s, name: %s, month: %d, year: %d)".format( number, name, month, year)

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
		(xml \ "expiration" \ "month" text).toLong,
		(xml \"expiration" \ "year" text).toLong
	)
}
