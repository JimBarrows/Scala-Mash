package scala_mash.highrise_api.models

import org.joda.time.DateTime
import xml.NodeSeq
import xml.NodeSeq.Empty

import bizondemand.utils.models.internet.Parameter
import scala_mash.rest.{Ok, Created, RestException, HttpStatusCode}
import scala_mash.rest.util.Helpers.{optionalLong, optionalString, optionalInt, optionalDateTimeWithTimeZone, printWithTimeZone}

import scala_mash.highrise_api.models.enumerations.VisibleToValues
import VisibleToValues._


case class Company(override val id: Option[Long],
                name: String,
                override val background: Option[String],
                override val createdAt: Option[DateTime],
                override val updatedAt: Option[DateTime],
                override val visibleTo: Option[VisibleToValues],
                override val ownerId: Option[Int],
                override val groupId: Option[Int],
                override val authorId: Option[Int],
                override val contactData: ContactData) extends Party(id, background,createdAt,updatedAt,visibleTo,ownerId,groupId,authorId,contactData) {

	val apiId = (id.getOrElse(0) + "-" + name.replaceAll("[ \t]","-").toLowerCase)

	def toPartyXml = <company>
    	<name>{name}</name>
    	{parentFieldsAsXml}
 	</company>

	override def toXml = <company>
    	<name>{name}</name>
    	{parentFieldsAsXml}
 	</company>

}

object Company extends HighriseServices[Company] {
	
	def parse(node: NodeSeq) = {
		debug("Company.parse: {}", node)
    	Company(Some((node \ "id" text).toLong),
        	node \ "name" text,
        	optionalString(node, "background") ,
         	optionalDateTimeWithTimeZone( node, "created-at" ),
        	optionalDateTimeWithTimeZone( node, "updated-at" ),
					VisibleToValues.valueOf(node \ "visible-to" text),
        	optionalInt(node, "owner-id"),
        	optionalInt(node, "group-id"),
        	optionalInt(node, "author-id"),
        	ContactData.parseResponse(node \ "contact-data")
        )
	}

	def parseList(node: NodeSeq) = {
		(node \\ "company").map( parse(_)).toList
	}

	def create(company: Company, account: Account): Company = {
		debug("Company.create: {}, {}", company, account)
		post(url +< (account.siteName) +/ "companies.xml", 
			Some(account.apiKey), 
			Some("x"), 
			company.toXml
		) match {
			case n:Created => parse( convertResponseToXml(n.response))
			case n :HttpStatusCode=> defaultStatusHandler(n)
		} 
	}

 	def show(id: Long, account: Account): Company = {
		debug("Company.show: {}, {}", id, account)
 		get(url +< (account.siteName) +/ ("companies") +/ ( id.toString + ".xml"), 
 			Some(account.apiKey), 
 			Some("x")
 		) match {
			case n:Ok => parse( convertResponseToXml(n.response))
			case n :HttpStatusCode=> defaultStatusHandler(n)
		} 
	}

 	def listAll(account: Account): List[Company] = {
		debug("Company.listAll: {}", account)
 		get(url +< (account.siteName) +/ "companies.xml", 
 			Some(account.apiKey), 
 			Some("x")
 		) match {
			case n:Ok => parseList( convertResponseToXml(n.response))
			case n :HttpStatusCode=> defaultStatusHandler(n)
		} 
	}

	def findPeopleByEmail(email: String, account: Account) : List[Company] = {
		debug("Company.findPeopleByEmail: {}, {}", email, account)
		get(url +< (account.siteName) +/ ("companies") +/( "search.xml") +& Parameter("criteria[email]", email), 
			Some(account.apiKey), 
			Some("x")
		)match {
			case n:Ok => parseList( convertResponseToXml(n.response))
			case n :HttpStatusCode=> defaultStatusHandler(n)
		} 
	}

	def update(company: Company, account: Account) = {
		debug("Company.update: {}, {}", company, account)
		put(url +< (account.siteName) +/ "companies" +/ (company.id.getOrElse(0).toString() + ".xml"), 
			Some(account.apiKey), 
			Some("x"),
			company.toXml
		)match {
			case n:Ok => 
			case n :HttpStatusCode=> defaultStatusHandler(n)
		} 
	}

	def destroy( person: Company, account: Account) = {
		debug("Company.update: {}, {}", person, account)
		delete(url +< (account.siteName) +/  ("companies") +/ ( person.id.getOrElse(0) + ".xml"), 
			Some(account.apiKey), 
			Some("x")
		) match {
			case n:Ok => 
			case n => defaultStatusHandler(n)
		}
	}

}
