package specs

import org.specs.Specification

import org.joda.time._

import bizondemand.utils.models.internet.{Url, DomainName, Http}

import scala_mash.highrise_api._
import models._
import enumerations.VisibleToValues._
import enumerations.NoteSubjectType
import enumerations.CollectionType

object NotesSpec extends Specification {

	"The Notes class" should {
		"create xml with attachments" in {
			basicNoteWithAttachments.toXml must equalIgnoreSpace(  basicNoteXmlWithAttachments)
		}

		"create xml with out attachments" in {
			basicNoteWithOutAttachments.toXml must equalIgnoreSpace(  basicNoteXmlWithOutAttachments)
		}
	}

	val basicNoteXmlWithAttachments = 
		<note>
			<id type="integer">1</id>
			<body>Hello world!</body>
			<author-id type="integer">3</author-id>
			<subject-id type="integer">1</subject-id>
			<subject-type>Party</subject-type>
			<subject-name>John Doe</subject-name>
			<collection-id type="integer">1</collection-id>
			<collection-type>Deal</collection-type>
			<visible-to>Everyone</visible-to>
			<owner-id type="integer">1</owner-id>
			<group-id type="integer">1</group-id>
			<attachments>
				<attachment>
					<id type="integer">1</id>
					<url>http://example.highrisehq.com/files/1</url>
					<name>picture.png</name>
					<size type="integer">72633</size>
				</attachment>
				<attachment>
 					<id type="integer">2</id>
					<url>http://example.highrisehq.com/files/2</url>
					<name>document.txt</name>
					<size type="integer">8837</size>
				</attachment>
			</attachments>
		</note>
	
	val basicNoteWithAttachments = Notes(
		Some(1),							//id: Option[Int]
		"Hello world!", 		//body: String
		Some(3), 							//authorId: Option[Int]
		Some(1), 							//subjectId: Option[Int]
		Some(NoteSubjectType.Party), 							//subjectType: Option[NoteSubjectType]
		Some("John Doe"), 							//subjectName: Option[String]
		Some(1), 							//collectionId: Option[Int]
		Some(CollectionType.Deal), 							//collectionId: Option[Int]
		Everyone, 					//visibleTo: VisibleToValues
		Some(1), 							//ownerId: Option[Int]  //userId -- when visible-to is "owner"
		Some(1), 							//groupId: Option[Int]  //groupId -- when visible-to is "NamedGroup"
		None, 							//createdAt: Option[DateTime]
		None, 							//updatedAt: Option[DateTime]
		Attachment(Some(1), Url("http://example.highrisehq.com/files/1"), "picture.png", 72633) ::
		Attachment(Some(2), Url("http://example.highrisehq.com/files/2"), "document.txt", 8837) ::
		Nil

	)

	val basicNoteXmlWithOutAttachments = 
		<note>
			<id type="integer">1</id>
			<body>Hello world!</body>
			<author-id type="integer">3</author-id>
			<subject-id type="integer">1</subject-id>
			<subject-type>Party</subject-type>
			<subject-name>John Doe</subject-name>
			<collection-id type="integer">1</collection-id>
			<collection-type>Deal</collection-type>
			<visible-to>Everyone</visible-to>
			<owner-id type="integer">1</owner-id>
			<group-id type="integer">1</group-id>
		</note>
	
	val basicNoteWithOutAttachments = Notes(
		Some(1),							//id: Option[Int]
		"Hello world!", 		//body: String
		Some(3), 							//authorId: Option[Int]
		Some(1), 							//subjectId: Option[Int]
		Some(NoteSubjectType.Party), 							//subjectType: Option[NoteSubjectType]
		Some("John Doe"), 							//subjectName: Option[String]
		Some(1), 							//collectionId: Option[Int]
		Some(CollectionType.Deal), 							//collectionId: Option[Int]
		Everyone, 					//visibleTo: VisibleToValues
		Some(1), 							//ownerId: Option[Int]  //userId -- when visible-to is "owner"
		Some(1), 							//groupId: Option[Int]  //groupId -- when visible-to is "NamedGroup"
		None, 							//createdAt: Option[DateTime]
		None, 							//updatedAt: Option[DateTime]
		Nil

	)
}
