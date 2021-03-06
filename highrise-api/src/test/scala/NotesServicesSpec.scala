package specs

import org.specs.Specification

import org.joda.time._

import bizondemand.utils.models.internet.{Url, DomainName, Http}

import scala_mash.highrise_api._
import models._
import enumerations.VisibleToValues._
import enumerations.NoteSubjectType
import enumerations.CollectionType

import scala_mash.rest.util.Helpers._


object NoteServicesSpec extends Specification {

	"The Notes services" should {

		shareVariables()
		setSequential()

		var personNote :Option[Note] = None
		var companyNote :Option[Note] = None
		var dealNote :Option[Note] = None

		doFirst { 
			person = Person.create( person, account)
			company = Company.create( company, account)
			deal = Deal.create( deal, account)
		}


		"create a note on a person" in {
			val id: Long = person.id.getOrElse(0).asInstanceOf[Long]
			val note = Note( "Hello world", id, NoteSubjectType.Party)

			val createdNote = Note.create( note, account)

			createdNote.id must beSome[Long]

			createdNote.body must_==( note.body)
			personNote=Some(createdNote)
		}

		"get a note on a person" in {
			personNote.map( note => {
				val gottenNote = Note.show( note.id.getOrElse(0), account)
				gottenNote must beSome[ Note]
			})
		}

		"list all notes on a person" in {
			personNote.map( note => {
				val gottenNote = Note.listAllNotesFor( person, None, account).head
				gottenNote must be_==( note)
			})
		}

		"update a note on a person" in {
			personNote.map( note => {
				val changedNote = Note(
					note.id
					,"Different body"
					,note.authorId
					,note.subjectId
					,note.subjectType
					,note.subjectName
					,note.collectionId
					,note.collectionType
					,note.visibleTo
					,note.ownerId
					,note.groupId
					,note.createdAt
					,note.updatedAt
					,note.attachments)

				val gottenNote = Note.update( changedNote, account)
				gottenNote.body must be_==( changedNote.body)
			})
		}

		"delete a note on a person" in {
			personNote.map( note => {
				Note.destroy( note, account)
				val gottenNote = Note.show( note.id.getOrElse(0), account)
				gottenNote must beNone
			})
		}

		"create a note on a company" in {
			val id: Long = company.id.getOrElse(0).asInstanceOf[Long]
			val note = Note( "Hello world", id, NoteSubjectType.Party)

			val createdNote = Note.create( note, account)

			createdNote.id must beSome[Long]

			createdNote.body must_==( note.body)
			companyNote=Some(createdNote)
		}

		"get a note on a company" in {
			companyNote.map( note => {
				val gottenNote = Note.show( note.id.getOrElse(0), account)
				gottenNote must beSome[ Note]
			})
		}

		"list all notes on a company" in {
			companyNote.map( note => {
				val gottenNote = Note.listAllNotesFor( company, None, account).head
				gottenNote must be_==( note)
			})
		}

		"update a note on a company" in {
			companyNote.map( note => {
				val gottenNote = Note.update( note, account)
				gottenNote must be_==( note)
			})
		}

		"delete a note on a company" in {
			companyNote.map( note => {
				Note.destroy( note, account)
				val gottenNote = Note.show( note.id.getOrElse(0), account)
				gottenNote must beNone
			})
		}

		"create a note on a deal" in {

			val id: Long = deal.id.getOrElse(0).asInstanceOf[Long]

			val note = Note( "Hello world", id, NoteSubjectType.Deal)

			val createdNote = Note.create( note, account)

			createdNote.id must beSome[Long]

			createdNote.body must_==( note.body)
			dealNote=Some(createdNote)
		}

		"get a note on a deal" in {
			dealNote.map( note => {
				val gottenNote = Note.show( note.id.getOrElse(0), account)
				gottenNote must beSome[ Note]
			})
		}

		"list all notes on a deal" in {
			dealNote.map( note => {
				val gottenNote = Note.listAllNotesFor( deal, None, account).head
				gottenNote must be_==( note)
			})
		}

		"update a note on a deal" in {
			dealNote.map( note => {
				val gottenNote = Note.update( note, account)
				gottenNote must be_==( note)
			})
		}

		"delete a note on a deal" in {
			dealNote.map( note => {
				Note.destroy( note, account)
				val gottenNote = Note.show( note.id.getOrElse(0), account)
				gottenNote must beNone
			})
		}

		"get a note on a case" in {
//			kaseNote.map( note => {
//				val gottenNote = Note.show( note.id.getOrElse(0), account)
//				gottenNote must beSome[ Note]
//			})
		}
		"create a note on a case" in {
//			kaseNote = None
		}
		"list all notes on a case" in {
//			kaseNote.map( note => {
//				val gottenNote = Note.listAllNotesFor( kase, None, account).head
//				gottenNote must be_==( note)
//			})
		}

		"update a note on a case" in {
//			caseNote.map( note => {
//				val gottenNote = Note.update( note, account)
//				gottenNote must be_==( note)
//			})
		}

		"delete a note on a case" in {
//			caseNote.map( note => {
//				Note.destroy( note, account)
//				val gottenNote = Note.show( note.id.getOrElse(0), account)
//				gottenNote must beNone
//			})
		}

			Person.destroy(person, account)
			Company.destroy(company, account)
			Deal.destroy( deal, account)
	}


	val account = Account("TestAccountForMe", "1ad2fc1adf9e7fc1f342d0e431069af0")
		

	val expectedContactData = ContactData(None, None, None, None, None)

	var person = 	Person(None,
			"Joe", //first name
			"Tester", //last name
			"JoeTester Title", //title
			Some("Background"), //backgroun
			None, //companyId
			None, //createAt
			None, //updatedAt
			Some(Everyone), //visibleTo
			None, //ownerId
			None, //groupId
			None, //authorId
			expectedContactData
	)

	var company = Company(
			None, //id
			"Company",  //name
			Some("A popular company for random data"),  //background
			None,
			None,
			Some(Everyone), //visible to
			None,  //owner
			None,  //group
			Some(2),  //author
			ContactData(
					Some(EmailAddress(Some(1), "john.doe@example.com", scala_mash.highrise_api.models.AddressLocationValues.Work) :: Nil),
					Some(PhoneNumber(Some(2), "555-555-5555", scala_mash.highrise_api.models.PhoneLocationValues.Work) :: PhoneNumber(Some(3), "555-666-6666", scala_mash.highrise_api.models.PhoneLocationValues.Home) :: Nil),
			None,
			None,
			None
			)
    )

	var deal = Deal( None, //accountId : Option[Long], 
			None, //authorId: Option[Long], 
			None, //background: Option[String],
			None, //categoryId : Option[Long],
			None, //createdAt : Option[DateTime],
			None, //currency : Option[String],
			None, //duration : Option[Long],
			None, //groupId : Option[Long],
			None, //id : Option[Long],
			"Create Test Deal", //name : String,
			None, //ownerId : Option[Long],
			None, //partyId : Option[Long],
			None, //price :Option[Long],
			None, //priceType:Option[String],
			None, //responsiblePartyId:Option[Long],
			None, //status:Option[DealStatus], 
			None, //statusChangedOn: Option[LocalDate],
			None, //updatedAt:Option[DateTime],
			None, //visibleTo:Option[VisibleToValues],
			None, //dealCategory:Option[DealCategory]
			None
			,None
		)	

	
}
