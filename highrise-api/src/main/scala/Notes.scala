package scala_mash.highrise_api.models

import xml._
import  xml.NodeSeq
import  xml.NodeSeq._

import org.joda.time.DateTime

import scala_mash.highrise_api._
import models._
import enumerations.NoteSubjectType._
import enumerations.VisibleToValues._

import bizondemand.utils.models.internet.Url

case class Notes ( 
		id: Option[Int]
		, body: String
		, authorId: Option[Int]
		, subjectId: Option[Int]
		, subjectType: Option[NoteSubjectType]
		, subjectName: Option[String]
		, collectionId: Option[Int]
		, visibleTo: VisibleToValues
		, ownerId: Option[Int]  //userId -- when visible-to is "owner"
		, groupId: Option[Int]  //groupId -- when visible-to is "NamedGroup"
		, createdAt: Option[DateTime]
		, updatedAt: Option[DateTime]
		, attachments: List[Attachment]
		) {

}



case class Attachment(
		id: Option[Int]
		, url: Url
		, name: String
		, size: Int) {
}
		
