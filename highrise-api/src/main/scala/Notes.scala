package scala_mash.highrise_api.models

import xml._
import  xml.NodeSeq
import  xml.NodeSeq._

import org.joda.time.DateTime

import bizondemand.utils.logging.Log

import scala_mash.rest.util.Helpers._

import scala_mash.highrise_api._
import models._
import enumerations._
import NoteSubjectType._
import CollectionType._
import VisibleToValues._

import bizondemand.utils.models.internet.Url

case class Note ( 
		id: Option[Int]
		, body: String
		, authorId: Option[Int]
		, subjectId: Option[Int]
		, subjectType: Option[NoteSubjectType]
		, subjectName: Option[String]
		, collectionId: Option[Int]
		, collectionType: Option[CollectionType]
		, visibleTo: VisibleToValues
		, ownerId: Option[Int]  //userId -- when visible-to is "owner"
		, groupId: Option[Int]  //groupId -- when visible-to is "NamedGroup"
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
	
	def parse( xml: NodeSeq): Note = {
		debug("Note.parse: xml: {}", xml)
		Note(
			optionalInt( xml, "id")
			,xml \ "body" text
			,optionalInt( xml, "author-id")
			,optionalInt( xml, "subject-id")
			,NoteSubjectType.valueOf(xml \ "subject-type" text)
			,optionalString(xml, "subject-name")
			,optionalInt( xml, "collection-id")
			,CollectionType.valueOf(xml \ "collection-type" text)
			,VisibleToValues.valueOf( xml \ "visible-to" text).getOrElse(Everyone)
			,optionalInt( xml, "owner-id")
			,optionalInt( xml, "group-id")
			,optionalDateTimeWithTimeZone(xml, "created-at")
			,optionalDateTimeWithTimeZone(xml, "updated-at")
			,Attachment.parseList( xml \ "attachments")
		)
	}
}



case class Attachment (
		  id: Option[Int]
		,url: Url
		,name: String
		,size: Int) {

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
			optionalInt( xml, "id")
			,Url( xml \ "url" text)
			,xml \ "name" text
			,(xml \ "size" text).toInt
		)
	}

	def parseList( xml: NodeSeq): List[Attachment] = {
		debug("Attachment.parseList( xml: {})", xml)
		(xml \ "attachment" \\ "attachment").map(parse(_)).toList
	}
}
		
