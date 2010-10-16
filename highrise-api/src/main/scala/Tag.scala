package scala_mash.highrise_api.models

import xml.NodeSeq
import xml.NodeSeq._
import scala_mash.highrise_api.{Account, HighriseServices}

import scala_mash.highrise_api._

import scala_mash.rest.{Ok, Created, RestException}
import scala_mash.rest.util.Helpers.{optionalLong}

/**
	*@author Jim Barrows
	*@created Feb 13, 2009
	*@version 1.0
	*/


case class Tag( id : Option[Long], name:String) {
	
	def toXml = <tag>
		{id.map( v => <id type="integer">{v}</id>).getOrElse(Empty)} 
		<name>{name}</name>
	</tag>

}


object Tag extends HighriseServices[Tag] {
	
	def parse(node : NodeSeq) = {
		debug("Parsing: {}", node)
		Tag( Some((node \ "id" text).toLong), node \"name" text)		
	}	
	
	def parseList( node: NodeSeq) : List[Tag] = ( node \\ "tag").map(parse(_)).toList
	
	def listAll(account:Account): List[Tag] = {
		debug("Tag.listAll {}.", account)
		get( url +< account.siteName +/ ("tags.xml"), 
			Some(account.apiKey), 
			Some("x")
		)match {
			case n:Ok => parseList( convertResponseToXml(n.response))
			case n => defaultStatusHandler(n)
		}
	}
	
	def listTagsFor( person: Person, account: Account): List[Tag] = {
		debug("listTagsFor( person: {}, account: {}", person, account)
		person.id.map( id => {
			get( url +< account.siteName +/ "people" +/ id.toString +/ "tags.xml", 
				Some(account.apiKey), 
				Some("x")
			) match {
				case n:Ok => parseList( convertResponseToXml(n.response))
				case n => defaultStatusHandler(n)
			}		
		}).getOrElse( Nil)
	}

	def listTagsFor( company: Company, account: Account): List[Tag] = {
		debug("listTagsFor( company: {}, account: {}", company, account)
		company.id.map( id => {
			get( url +< account.siteName +/ "companies" +/ id.toString +/ "tags.xml", 
				Some(account.apiKey), 
				Some("x")
			) match {
				case n:Ok => parseList( convertResponseToXml(n.response))
				case n => defaultStatusHandler(n)
			}		
		}).getOrElse( Nil)
	}

	def listTagsFor( deal: Deal, account: Account): List[Tag] = {
		debug("listTagsFor( deal: {}, account: {}", deal, account)
		deal.id.map( id => {
			get( url +< account.siteName +/ "deal" +/ id.toString +/ "tags.xml", 
				Some(account.apiKey), 
				Some("x")
			) match {
				case n:Ok => parseList( convertResponseToXml(n.response))
				case n => defaultStatusHandler(n)
			}		
		}).getOrElse( Nil)
	}
	
	def addTagTo(person:Person, tag:Tag, account:Account):Tag ={
		debug("Tag.addTag(person: {}  tag: {}  account: {}", person, tag, account)
		person.id.map( id => {
			post ( url +<  (account.siteName) +/ "people" +/ id.toString +/ "tags.xml", 
				Some(account.apiKey), 
				Some("x"), 
				<name>{tag.name}</name>
			)match {
				case n:Created => parse( convertResponseToXml(n.response))
				case n => defaultStatusHandler(n)
			}		
		}).getOrElse( throw NoIdException( person))
	}
		
	def addTagTo(company:Company, tag:Tag, account:Account):Tag ={
		debug("Tag.addTag(company: {}  tag: {}  account: {}", company, tag, account)
		company.id.map( id => {
			post ( url +<  (account.siteName) +/ "companies" +/ id.toString +/ "tags.xml", 
				Some(account.apiKey), 
				Some("x"), 
				<name>{tag.name}</name>
			)match {
				case n:Created => parse( convertResponseToXml(n.response))
				case n => defaultStatusHandler(n)
			}		
		}).getOrElse( throw NoIdException( company))
	}
		
	def addTagTo(deal:Deal, tag:Tag, account:Account):Tag ={
		debug("Tag.addTag(deal: {}  tag: {}  account: {}", deal, tag, account)
		deal.id.map( id => {
			post ( url +<  (account.siteName) +/ "deals" +/ id.toString +/ "tags.xml", 
				Some(account.apiKey), 
				Some("x"), 
				<name>{tag.name}</name>
			)match {
				case n:Created => parse( convertResponseToXml(n.response))
				case n => defaultStatusHandler(n)
			}		
		}).getOrElse( throw NoIdException( deal))
	}
		
	def removeTag(person:Person, tag:Tag, account:Account)  ={ 
		debug("Tag.removeTag( person {}, tag {}, account: {})",person, tag, account)
		person.id.map( personId => {
			tag.id.map( tagId => {
				delete( url +< (account.siteName) +/ "people" +/ personId.toString +/ "tags" +/ "%d.xml".format(tagId), 
					Some(account.apiKey), 
					Some("x")
				)match {
					case n:Ok => 
					case n => defaultStatusHandler(n)
				}
			}).getOrElse( throw NoIdException( tag))
		}).getOrElse( throw NoIdException( person))
	}

	def removeTag(company:Company, tag:Tag, account:Account)  ={ 
		debug("Tag.removeTag( company {}, tag {}, account: {})", company, tag, account)
		company.id.map( companyId => {
			tag.id.map( tagId => {
				delete( url +< (account.siteName) +/ "companies" +/ companyId.toString +/ "tags" +/ "%d.xml".format(tagId), 
					Some(account.apiKey), 
					Some("x")
				)match {
					case n:Ok => 
					case n => defaultStatusHandler(n)
				}
			}).getOrElse( throw NoIdException( tag))
		}).getOrElse( throw NoIdException( company))
	}

	def removeTag(deal:Deal, tag:Tag, account:Account)  ={ 
		debug("Tag.removeTag( deal {}, tag {}, account: {})", deal, tag, account)
		deal.id.map( dealId => {
			tag.id.map( tagId => {
				delete( url +< (account.siteName) +/ "deals" +/ dealId.toString +/ "tags" +/ "%d.xml".format(tagId), 
					Some(account.apiKey), 
					Some("x")
				)match {
					case n:Ok => 
					case n => defaultStatusHandler(n)
				}
			}).getOrElse( throw NoIdException( tag))
		}).getOrElse( throw NoIdException( deal))
	}
}
