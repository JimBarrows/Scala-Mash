package scala_mash.highrise_api.models

import xml._
import  xml.NodeSeq
import  xml.NodeSeq._

import org.joda.time.{DateTime, LocalDate}

import scala_mash.highrise_api._
import scala_mash.highrise_api.Utils._

import scala_mash.rest.util.Helpers.{optionalDateTimeWithTimeZone, printWithTimeZone, printYmd, optionalParseYmd, optionalLong,optionalString, optionalYmd}

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
				party : Option[Party]  //customer involved
//				parties : Option[List[Party]]  //all parties involved
				) {
	def toXml:NodeSeq =	<deal>
  		<account-id type="integer">{accountId.getOrElse(Empty)}</account-id>
  		<author-id type="integer">{authorId.getOrElse(Empty)}</author-id>
  		<background>{background.getOrElse(Empty)}</background>
  		<category-id type="integer">{categoryId.getOrElse(Empty)}</category-id>
  		<created-at type="datetime">{createdAt.map(printWithTimeZone(_)).getOrElse(Empty)}</created-at>
  		<currency>{currency.getOrElse(Empty)}</currency>
  		<duration type="integer">{duration.getOrElse(Empty)}</duration>
  		<group-id type="integer">{groupId.getOrElse(Empty)}</group-id>
  		<id type="integer">{id.getOrElse(Empty)}</id>
  		<name>{name}</name>
  		<owner-id type="integer">{ownerId.getOrElse(Empty)}</owner-id>
  		<party-id type="integer">{partyId.getOrElse(Empty)}</party-id>
  		<price type="integer">{price.getOrElse(Empty)}</price>
  		<price-type>{priceType.getOrElse(Empty)}</price-type>
  		<responsible-party-id type="integer">{responsiblePartyId.getOrElse(Empty)}</responsible-party-id>
  		<status>{status.getOrElse(Empty)}</status>
  		<status-changed-on type="date">{statusChangedOn.map(printYmd(_)).getOrElse(Empty)}</status-changed-on>
  		<updated-at type="datetime">{updatedAt.map(printWithTimeZone(_)).getOrElse(Empty)}</updated-at>
  		<visible-to>{visibleTo.getOrElse(Empty)}</visible-to>
  		{
  			dealCategory.map( cat =>
  				<category>
  					<id type="integer">{cat.id.getOrElse(Empty)}</id>
  					<name>{cat.name}</name>
  				</category>
  			).getOrElse(Empty)

				party.map( p => 
					p match {
						case person : Person => person.toXml
						case company : Company => company.toXml
					}
				)
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
			optionalParty(node)//,
//			optionalParties(node)
		)
	}

	def optionalParty( node:NodeSeq ):Option[Party] = if( (node \ "party" text).isEmpty) None else Some( Party.parse( node \ "party"))

	def optionalParties( node:NodeSeq ):Option[List[Party]] = None //if( (node \ "parties" text).isEmpty) None else Some( Party.parseList( node \ "parties"))
	
	
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
	
	def destroy(id:Long, account:Account) = {
		debug("Deal.destroy id: {}, account: {}", id, account)
		delete( url +< (account.siteName) +/ "deals" +/ (id.toString + ".xml"), 
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

