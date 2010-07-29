package scala_mash.highrise_api.models

import xml._
import  xml.NodeSeq
import  xml.NodeSeq._

import org.joda.time.{DateTime, LocalDate}

import scala_mash.highrise_api._
import scala_mash.highrise_api.Utils._

import bizondemand.utils.models.internet.Url
import scala_mash.rest.{Ok, Created, RestException}
import scala_mash.rest.util.Helpers.{optionalLong,optionalString}

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
				dealCategory:Option[DealCategory]
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
  		
		}
	</deal>
}

object Deal extends HighriseServices[Deal] {
	
	def parse(node: NodeSeq) = {
		Deal(
			optionalLong(node, "account-id" ),
			optionalLong(node, "author-id" ),
			optionalString(node, "background" ),
			optionalLong(node, "category-id"),
			optionalParseDateTimeWithTimeZone(node, "created-at"),
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
			optionalParseYmd( node, "status-changed-on"),
			optionalParseDateTimeWithTimeZone(node, "updated-at"),
			VisibleToValues.valueOf( node \ "visible-to" text),
			Some(DealCategory.parse( node \ "category"))			
		)
	}
	
	def parseList(node: NodeSeq) = (node \\ "deal").map( parse(_)).toList
	
	def create( deal:Deal, account:Account) : Deal = {
		var statusCode = post( url +< (account.siteName) +/ "deals.xml", 
			Some(account.apiKey), 
			Some("x"), 
			deal.toXml
		)
		println("statusCode: " + statusCode)
		statusCode match {
			case n:Created => getByUrl( n.location, account, parse _)
			case n => defaultStatusHandler(n)
		} 
	}	
	
	def destroy(id:Long, account:Account) = {
		delete( url +< (account.siteName) +/ "deals" +/ (id.toString + ".xml"), 
			Some(account.apiKey), 
			Some("x")
		)match {
			case n:Ok => 
			case n => defaultStatusHandler(n)
		} 
	}
	
	def listAll(account :Account) : List[Deal] = {
		get(url +< (account.siteName) +/ "deals.xml", 
			Some(account.apiKey), 
			Some("x")
		)match {
			case n:Ok => parseList( convertResponseToXml(n.response))
			case n => defaultStatusHandler(n)
		} 
	}
	
	def show( id:Long, account :Account) :Deal = {
		get(url +< (account.siteName) +/ ("deals") +/ ( id.toString + ".xml"), 
			Some(account.apiKey), 
			Some("x")
		)match {
			case n:Ok => parse( convertResponseToXml(n.response))
			case n => defaultStatusHandler(n)
		} 
	}
		
	def statusUpdate(id:Long, status:DealStatus, account :Account) = {
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
		put(url +< (account.siteName) +/ "deals" +/ (deal.id.getOrElse(0).toString() + ".xml"), 
			Some(account.apiKey), 
			Some("x"), deal.toXml
		)match {
			case n:Ok => 
			case n => defaultStatusHandler(n)
		}
		
	}	
}

