package scala_mash.highrise_api.models

import xml._
import  xml.NodeSeq
import  xml.NodeSeq._

import org.joda.time.DateTime

import bizondemand.utils.logging.Log

import scala_mash.rest._
import scala_mash.rest.util.Helpers._

import scala_mash.highrise_api._
import models._
import enumerations._
import NoteSubjectType._
import CollectionType._
import VisibleToValues._

import bizondemand.utils.models.internet.{Url, Parameter}

case class Note ( 
		id: Option[Long]
		, body: String
		, authorId: Option[Long]
		, subjectId: Option[Long]
		, subjectType: Option[NoteSubjectType]
		, subjectName: Option[String]
		, collectionId: Option[Long]
		, collectionType: Option[CollectionType]
		, visibleTo: VisibleToValues
		, ownerId: Option[Long]  //userId -- when visible-to is "owner"
		, groupId: Option[Long]  //groupId -- when visible-to is "NamedGroup"
		, createdAt: Option[DateTime]
		, updatedAt: Option[DateTime]
		, attachments: List[Attachment]
		) {


	def toXml = 
		<note>
			{id.map( v => <id type="integer">{v}</id>).getOrElse(Empty)}
			<body>{body}</body>
			{authorId.map( v => <author-id type="integer">{v}</author-id>).getOrElse(Empty)}
			{subjectId.map( v => <subject-id type="integer">{v}</subject-id>).getOrElse(Empty)}
			{subjectType.map( v => <subject-type>{v}</subject-type>).getOrElse(Empty)}
			{subjectName.map( v => <subject-name>{v}</subject-name>).getOrElse(Empty)}
			{collectionId.map( v => <collection-id type="integer">{v}</collection-id>).getOrElse(Empty)}
			{collectionType.map( v => <collection-type>{v}</collection-type>).getOrElse(Empty)}
			<visible-to>{visibleTo}</visible-to>
			{ownerId.map( v => <owner-id type="integer">{v}</owner-id>).getOrElse(Empty)}
			{groupId.map( v => <group-id type="integer">{v}</group-id>).getOrElse(Empty)}
			{
				attachments match {
					case Nil => Empty
					case l: List[Attachment] => 
						<attachments>
							{ l.map( v => v.toXml) }
						</attachments>
				}
			}
		</note>
}


object Note extends HighriseServices[Note]{

	def apply( body: String, subjectId: Long, subjectType: NoteSubjectType): Note = Note( 
			  None							//id: Option[Long]
			, body		//body: String
			, None							//authorId: Option[Long]
			, Some(subjectId)							//subjectId: Option[Long]
			, Some(subjectType)							// subjectType: Option[NoteSubjectType]
			, None							//subjectName: Option[String]
			, None							//collectionId: Option[Long]
			, None							//collectionType: Option[CollectionType]
			, Everyone					//visibleTo: VisibleToValues
			, None							//ownerId: Option[Long]  //userId -- when visible-to is "owner"
			, None							//groupId: Option[Long]  //groupId -- when visible-to is "NamedGroup"
			, None							//createdAt: Option[DateTime]
			, None							//updatedAt: Option[DateTime]
			, Nil							//attachments: List[Attachment]
	)

	def create( note: Note, account:Account): Note = {
		debug("Note.create( note: {}, account: {}", note, account)

		val statusCode = post( url +< (account.siteName) +/ "notes.xml", Some(account.apiKey), Some("x"), note.toXml) 
		debug("Note.create statusCode: {}", statusCode)
		statusCode match {
			case n:Created => getByUrl(Url("%s.xml".format(n.location.toString)), account, parse _)
			case n => defaultStatusHandler(n)
		}

	}

	def show( id: Long, account:Account): Option[Note] = {
		debug("Note.show( id: {}, account: {}", id, account)

		val statusCode = get( url +< (account.siteName) +/ "notes" +/ "%d.xml".format(id), 
				Some(account.apiKey), 
				Some("x")) 
		debug("Note.show statusCode: {}", statusCode)

		statusCode match {
			case n:Ok => Some(parse( convertResponseToXml(n.response)))
			case n:NotFound => None
			case n => defaultStatusHandler(n)
		}

	}

	def listAllNotesFor( person: Person, pageSize:Option[Long], account: Account):List[Note] = {
		debug("Note.listAllNotesFor( person: {}, account: {}", person, account)

		person.id.map( id => {

			val listUrl = pageSize.map( size => {
					url +< (account.siteName) +/ "people" +/ id.toString +/ "notes.xml" +&(Parameter("n", size))
				}
			).getOrElse( url +< (account.siteName) +/ "people" +/ id.toString +/ "notes.xml")

			val statusCode = get( listUrl,
					Some(account.apiKey), 
					Some("x")) 
			debug("Note.listAllNotesFor statusCode: {}", statusCode)

			statusCode match {
				case n:Ok => parseList( convertResponseToXml(n.response))
				case n => defaultStatusHandler(n)
			}
		}).getOrElse( Nil)
	}

	def listAllNotesFor( company: Company, pageSize:Option[Long], account: Account):List[Note] = {
		debug("Note.listAllNotesFor( company: {}, account: {}", company, account)

		company.id.map( id => {

			val listUrl = pageSize.map( size => {
					url +< (account.siteName) +/ "companies" +/ id.toString +/ "notes.xml" +&(Parameter("n", size))
				}
			).getOrElse( url +< (account.siteName) +/ "companies" +/ id.toString +/ "notes.xml")

			val statusCode = get( listUrl,
					Some(account.apiKey), 
					Some("x")) 
			debug("Note.listAllNotesFor statusCode: {}", statusCode)

			statusCode match {
				case n:Ok => parseList( convertResponseToXml(n.response))
				case n => defaultStatusHandler(n)
			}
		}).getOrElse( Nil)
	}


	def listAllNotesFor( deal: Deal, pageSize:Option[Long], account: Account):List[Note] = {
		debug("Note.listAllNotesFor( deal: {}, account: {}", deal, account)

		deal.id.map( id => {

			val listUrl = pageSize.map( size => {
					url +< (account.siteName) +/ "deals" +/ id.toString +/ "notes.xml" +&(Parameter("n", size))
				}
			).getOrElse( url +< (account.siteName) +/ "deals" +/ id.toString +/ "notes.xml")

			val statusCode = get( listUrl,
					Some(account.apiKey), 
					Some("x")) 
			debug("Note.listAllNotesFor statusCode: {}", statusCode)

			statusCode match {
				case n:Ok => parseList( convertResponseToXml(n.response))
				case n => defaultStatusHandler(n)
			}
		}).getOrElse( Nil)
	}

	def update( note: Note, account:Account): Note = {
		debug("Note.update( note: {}, account: {}", note, account)

		note.id.map( id => {
			val noteXml = <note><body>{note.body}</body></note>
			val statusCode = put( url +< (account.siteName) +/ "notes" +/ "%d.xml".format(id), 
					Some(account.apiKey), 
					Some("x"),
					noteXml) 
			debug("Note.show statusCode: {}", statusCode)

			statusCode match {
				case n:Ok => show( id, account).getOrElse( note)
				case n => defaultStatusHandler(n)
			}
		}).getOrElse( throw NoIdException( note))

	}

	def destroy( note: Note, account:Account) = {
		debug("Note.delete( note: {}, account: {}", note, account)

		note.id.map( id => {
			val statusCode = delete( url +< (account.siteName) +/ "notes" +/ "%d.xml".format(id), 
					Some(account.apiKey), 
					Some("x")) 
			debug("Note.show statusCode: {}", statusCode)

			statusCode match {
				case n:Ok => 
				case n => defaultStatusHandler(n)
			}
		}).getOrElse( throw NoIdException( note))

	}

	def parseList( xml: NodeSeq): List[Note] = {
		( xml \\ "note").map( parse( _)).toList
	}

	def parse( xml: NodeSeq): Note = {
		debug("Note.parse: xml: {}", xml)
		Note(
			optionalLong( xml, "id")
			,xml \ "body" text
			,optionalLong( xml, "author-id")
			,optionalLong( xml, "subject-id")
			,NoteSubjectType.parse(xml, "subject-type" )
			,optionalString(xml, "subject-name")
			,optionalLong( xml, "collection-id")
			,CollectionType.parse(xml, "collection-type" )
			,VisibleToValues.parse( xml, "visible-to").getOrElse(Everyone)
			,optionalLong( xml, "owner-id")
			,optionalLong( xml, "group-id")
			,optionalDateTimeWithTimeZone(xml, "created-at")
			,optionalDateTimeWithTimeZone(xml, "updated-at")
			,Attachment.parseList( xml \ "attachments")
		)
	}
}



case class Attachment (
		  id: Option[Long]
		,url: Url
		,name: String
		,size: Long) {

	def toXml =
		<attachment>
			{id.map( v => <id type="integer">{v}</id>).getOrElse(Empty)}
			<name>{name}</name>
			<size type="integer">{size}</size>
			<url>{url}</url>
		</attachment>
}

object Attachment extends Log{

	def parse( xml: NodeSeq): Attachment = {
		debug("Attachment.parse( xml: {})", xml)
		Attachment(
			optionalLong( xml, "id")
			,Url( xml \ "url" text)
			,xml \ "name" text
			,(xml \ "size" text).toLong
		)
	}

	def parseList( xml: NodeSeq): List[Attachment] = {
		debug("Attachment.parseList( xml: {})", xml)
		(xml \ "attachment" \\ "attachment").map(parse(_)).toList
	}
}
		
