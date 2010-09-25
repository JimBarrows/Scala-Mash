package scala_mash.highrise_api.models

import scala_mash.highrise_api.Utils._
import org.joda.time.DateTime
import xml.NodeSeq
import xml.NodeSeq.Empty

import bizondemand.utils.models.internet.Parameter
import scala_mash.rest.{Ok, Created, RestException, HttpStatusCode}
import scala_mash.rest.util.Helpers.{optionalLong, optionalString, optionalInt, optionalDateTimeWithTimeZone, printWithTimeZone}

import scala_mash.highrise_api.models.enumerations.VisibleToValues
import VisibleToValues._


case class Person(override val id: Option[Long],
                firstName: String,
                lastName: String,
                title: String,
                override val background: Option[String],
                companyId: Option[Int],
                override val createdAt: Option[DateTime],
                override val updatedAt: Option[DateTime],
                override val visibleTo: Option[VisibleToValues],
                override val ownerId: Option[Int],
                override val groupId: Option[Int],
                override val authorId: Option[Int],
                override val contactData: ContactData) extends Party(id, background,createdAt,updatedAt,visibleTo,ownerId,groupId,authorId,contactData) {

	val apiId = (id.getOrElse(0) + "-" + firstName + "-" + lastName).toLowerCase

	def toXml = <person>
    	<first-name>{firstName}</first-name>
    	<last-name>{lastName}</last-name>
    	<title>{title}</title>
    	<company-id type="integer">{companyId.getOrElse("")}</company-id>
    	{super.fieldsAsXml}
 	</person>

}

object Person extends HighriseServices[Person] {
	
	def parse(node: NodeSeq) = {
		debug("Person.parse: {}", node)
    	Person(Some((node \ "id" text).toLong),
        	node \ "first-name" text,
        	node \ "last-name" text,
        	node \ "title" text,
        	optionalString(node, "background") ,
        	optionalInt( node, "company-id"),
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
		(node \\ "person").map( parse(_)).toList
	}

	def create(person: Person, account: Account): Person = {
		debug("Person.create: {}, {}", person, account)
		post(url +< (account.siteName) +/ "people.xml", 
			Some(account.apiKey), 
			Some("x"), 
			person.toXml
		) match {
			case n:Created => parse( convertResponseToXml(n.response))
			case n :HttpStatusCode=> defaultStatusHandler(n)
		} 
	}

 	def show(id: Long, account: Account): Person = {
		debug("Person.show: {}, {}", id, account)
 		get(url +< (account.siteName) +/ ("people") +/ ( id.toString + ".xml"), 
 			Some(account.apiKey), 
 			Some("x")
 		) match {
			case n:Ok => parse( convertResponseToXml(n.response))
			case n :HttpStatusCode=> defaultStatusHandler(n)
		} 
	}

 	def listAll(account: Account): List[Person] = {
		debug("Person.listAll: {}", account)
 		get(url +< (account.siteName) +/ "people.xml", 
 			Some(account.apiKey), 
 			Some("x")
 		) match {
			case n:Ok => parseList( convertResponseToXml(n.response))
			case n :HttpStatusCode=> defaultStatusHandler(n)
		} 
	}

	def findPeopleByEmail(email: String, account: Account) : List[Person] = {
		debug("Person.findPeopleByEmail: {}, {}", email, account)
		get(url +< (account.siteName) +/ ("people") +/( "search.xml") +& Parameter("criteria[email]", email), 
			Some(account.apiKey), 
			Some("x")
		)match {
			case n:Ok => parseList( convertResponseToXml(n.response))
			case n :HttpStatusCode=> defaultStatusHandler(n)
		} 
	}

	def update(person: Person, account: Account) = {
		debug("Person.update: {}, {}", person, account)
		put(url +< (account.siteName) +/ "people" +/ (person.id.getOrElse(0).toString() + ".xml"), 
			Some(account.apiKey), 
			Some("x"),
			person.toXml
		)match {
			case n:Ok => 
			case n :HttpStatusCode=> defaultStatusHandler(n)
		} 
	}
}
