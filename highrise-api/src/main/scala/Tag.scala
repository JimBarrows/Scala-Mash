package scala_mash.highrise_api.models

import xml.NodeSeq
import scala_mash.highrise_api.{Account, HighriseServices}

import scala_mash.highrise_api._
import scala_mash.highrise_api.Utils._

import scala_mash.rest.{Ok, Created, RestException}
import scala_mash.rest.util.Helpers.{optionalLong}

/**
	*@author Jim Barrows
	*@created Feb 13, 2009
	*@version 1.0
	*/

object SubjectType extends Enumeration {
	type SubjectType = Value
	val Person = Value("people")
	val Company = Value("company")
	val Kase = Value("kase")
	val Deal = Value("deal")
}

import SubjectType._

case class Tag( id : Option[Long], name:String) {
	
	def toXml = <tag>
		<id type="integer">{id.getOrElse(0)}</id>
		<name>{name}</name>
	</tag>

}


object Tag extends HighriseServices[Tag] {
	
	def parse(node : NodeSeq) = {
		debug("Parsing: {}", node)
		Tag( Some((node \ "id" text).toLong), node \"name" text)		
	}	
	
	/**Normally the API for a single item (ie person, company etc) would return that record, in this case it returns a list of people,
	 * so fake out the system so it works ok.
	*/
	def fakeParse( node: NodeSeq):Tag = Tag(None, "FAKETAGFAKETAG")

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
	
	def listTagsForAPerson( person : Person, account:Account) = {
		debug("Listing all tags for person: {}",person)
		get( url +< account.siteName +/ ("people") +/ (person.id.getOrElse(0).toString) +/ ("tags.xml"), 
			Some(account.apiKey), 
			Some("x")
		) match {
			case n:Ok => parseList( convertResponseToXml(n.response))
			case n => defaultStatusHandler(n)
		}		
	}
	
	def addTag(person:Person, tag:Tag, account:Account):Tag ={
		debug("Tag.addTag(person: {}  tag: {}  account: {}", person, tag, account)
		post ( url +<  (account.siteName) +/ SubjectType.Person.toString +/ person.apiId +/ "tags.xml", 
			Some(account.apiKey), 
			Some("x"), 
			<name>{tag.name}</name>
		)match {
			case n:Created => parse( convertResponseToXml(n.response))
			case n => defaultStatusHandler(n)
		}		
	}
		
	def removeTag(person:Person, tag:Tag, account:Account)  ={ 
		debug("Tag.removeTag( tag {} from {})",person, tag)
		delete( url +< (account.siteName) +/ SubjectType.Person.toString +/ person.apiId +/ "tags" +/ (tag.id.toString + ".xml"), 
			Some(account.apiKey), 
			Some("x")
		)match {
			case n:Ok => 
			case n => defaultStatusHandler(n)
		}
		
	}

}
