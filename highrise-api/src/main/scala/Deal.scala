package scala_mash.highrise_api.models

import xml._
import  xml.NodeSeq
import  xml.NodeSeq._

import org.joda.time.{DateTime, LocalDate}

import scala_mash.highrise_api._

import scala_mash.rest.util.Helpers.{optionalDateTimeWithTimeZone, printWithTimeZone, printYmd, optionalLong,optionalString, optionalYmd}

import bizondemand.utils.models.internet.Url
import scala_mash.rest.{Ok, Created, RestException}

import scala_mash.highrise_api.models.enumerations._
import VisibleToValues._
import DealStatus._

case class Deal( accountId : Option[Long], 
				authorId: Option[Long], 
				background: Option[String],
				categoryId : Option[Long],
				createdAt : Option[DateTime],
				currency : Option[String],
				duration : Option[Long],
				groupId : Option[Long],
				id : Option[Long],
				name : String,
				ownerId : Option[Long],
				partyId : Option[Long],
				price :Option[Long],
				priceType:Option[String],
				responsiblePartyId:Option[Long],
				status:Option[DealStatus], 
				statusChangedOn: Option[LocalDate],
				updatedAt:Option[DateTime],
				visibleTo:Option[VisibleToValues],
				dealCategory:Option[DealCategory],
				party : Option[Party],   //customer involved
				parties : Option[List[Party]]  //all parties involved
				) {


	def toXml:NodeSeq =	<deal>
  		{accountId.map(  v => <account-id type="integer">{v}</account-id>).getOrElse(Empty)}
  		{authorId.map(   v => <author-id type="integer">{v}</author-id>).getOrElse(Empty)}
  		{background.map( v => <background>{v}</background>).getOrElse(Empty)} 
  		{categoryId.map( v => <category-id type="integer">{v}</category-id>).getOrElse(Empty)} 
  		{createdAt.map(  v => <created-at type="datetime">{printWithTimeZone(v)}</created-at>).getOrElse(Empty)} 
  		{currency.map(   v => <currency>{v}</currency>).getOrElse(Empty)} 
  		{duration.map(   v => <duration type="integer">{v}</duration>).getOrElse(Empty)} 
  		{groupId.map(    v => <group-id type="integer">{v}</group-id>).getOrElse(Empty)} 
  		{id.map(         v => <id type="integer">{v}</id>).getOrElse(Empty)} 
  		<name>{name}</name>
  		{ownerId.map(    v => <owner-id type="integer">{v}</owner-id>).getOrElse(Empty)} 
  		{partyId.map(    v => <party-id type="integer">{v}</party-id>).getOrElse(Empty)} 
  		{price.map(      v => <price type="integer">{v}</price>).getOrElse(Empty)} 
  		{priceType.map(  v => <price-type>{v}</price-type>).getOrElse(Empty)} 
  		{responsiblePartyId.map( v => <responsible-party-id type="integer">{v}</responsible-party-id>).getOrElse(Empty)}
  		{status.map(     v => <status>{v}</status>).getOrElse(Empty)} 
  		{statusChangedOn.map( v => <status-changed-on type="date">{printYmd(v)}</status-changed-on>).getOrElse(Empty)}
  		{updatedAt.map(  v => <updated-at type="datetime">{printWithTimeZone(v)}</updated-at>).getOrElse(Empty)} 
  		{visibleTo.map(  v => <visible-to>{v}</visible-to>).getOrElse(Empty)} 
  		{
  			dealCategory.map( cat => {
  				<category>
  					<id type="integer">{cat.id.getOrElse(Empty)}</id>
  					<name>{cat.name}</name>
  				</category>
  			}
  			).getOrElse(Empty)
			} {
				party.map( p =>  {
					p match {
						case person : Person => person.toXml
						case company : Company => company.toXml
						case _  => Empty
					}
					}
				).getOrElse(Empty)
			} {
				parties.map( partyList =>  
					<parties>{
						partyList.map( p=>
								p match {
									case person : Person => person.toXml
									case company : Company => company.toXml
									case _  => Empty
								}
						)
					}</parties>
				).getOrElse(Empty) 
		}
	</deal>
}

object Deal extends HighriseServices[Deal] {
	
	def parse(node: NodeSeq) = {
		debug("Deal.parse: node: {}", node)
		Deal(
			optionalLong(node, "account-id" ),
			optionalLong(node, "author-id" ),
			optionalString(node, "background" ),
			optionalLong(node, "category-id"),
			optionalDateTimeWithTimeZone(node, "created-at"),
			optionalString(node, "currency"),
			optionalLong( node, "duration"),
			optionalLong( node, "group-id"),
			optionalLong(node, "id" ),
			node \ "name" text,
			optionalLong(node, "owner-id"),
			optionalLong(node, "party-id"),
			optionalLong( node, "price"),
			optionalString(node, "price-type"),
			optionalLong(node, "responsible-party-id"),
			DealStatus.valueOf(node \ "status" text),
			optionalYmd( node, "status-changed-on"),
			optionalDateTimeWithTimeZone(node, "updated-at"),
			VisibleToValues.valueOf( node \ "visible-to" text),
			(if( ( node \ "category" text).isEmpty )None else Some(DealCategory.parse( node \ "category"))),
			(if( ( node \ "party" text).isEmpty )None else Some(Party.parseList( node \ "party").first)),
			optionalParties(node)
		)
	}

	def optionalParty( node:NodeSeq ):Option[Party] = 
		if( (node \ "party" text).isEmpty) None else Some( Party.parse( node \ "party" ))

	def optionalParties( node:NodeSeq ):Option[List[Party]] = if( (node \ "parties" text).isEmpty) None else Some( Party.parseList( node \ "parties"))
	
	
	def parseList(node: NodeSeq) = (node \\ "deal").map( parse(_)).toList
	
	def create( deal:Deal, account:Account) : Deal = {
		debug("Deal.create deal: {}, account: {}", deal, account)
		var statusCode = post( url +< (account.siteName) +/ "deals.xml", 
				Some(account.apiKey), 
				Some("x"), 
				deal.toXml
			)
		println("Deal.create statusCode: " + statusCode)
		statusCode match {
			case n:Created => parse(convertResponseToXml(n.response))
			case n => defaultStatusHandler(n)
		} 
	}	
	
	def destroy(deal:Deal, account:Account) = {
		debug("Deal.destroy deal: {}, account: {}", deal, account)
		delete( url +< (account.siteName) +/ "deals" +/ (deal.id.getOrElse(0).toString + ".xml"), 
			Some(account.apiKey), 
			Some("x")
		)match {
			case n:Ok => 
			case n => defaultStatusHandler(n)
		} 
	}
	
	def listAll(account :Account) : List[Deal] = {
		debug("Deal.listAll account: {}", account)
		get(url +< (account.siteName) +/ "deals.xml", 
			Some(account.apiKey), 
			Some("x")
		)match {
			case n:Ok => parseList( convertResponseToXml(n.response))
			case n => defaultStatusHandler(n)
		} 
	}
	
	def show( id:Long, account :Account) :Deal = {
		debug("Deal.destroy id: {}, account: {}", id, account)
		get(url +< (account.siteName) +/ ("deals") +/ ( id.toString + ".xml"), 
			Some(account.apiKey), 
			Some("x")
		)match {
			case n:Ok => parse( convertResponseToXml(n.response))
			case n => defaultStatusHandler(n)
		} 
	}
		
	def statusUpdate(id:Long, status:DealStatus, account :Account) = {
		debug("Deal.statusUpdate id: {}, status: {} account: {}", id, status, account)
		put( url +< (account.siteName) +/ "deals" +/ id.toString +/ "status.xml", 
			Some(account.apiKey), 
			Some("x"), 
			<status><name>{status}</name></status>
		)match {
			case n:Ok => parse( convertResponseToXml(n.response))
			case n => defaultStatusHandler(n)
		}
	}
	
	def update( deal: Deal, account :Account) = {
		debug("Deal.update deal: {}, account: {}", deal, account)
		put(url +< (account.siteName) +/ "deals" +/ (deal.id.getOrElse(0).toString() + ".xml"), 
			Some(account.apiKey), 
			Some("x"), deal.toXml
		)match {
			case n:Ok => 
			case n => defaultStatusHandler(n)
		}
		
	}	
}

