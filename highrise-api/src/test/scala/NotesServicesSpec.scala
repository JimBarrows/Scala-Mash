package specs

import org.specs.Specification

import org.joda.time._

import bizondemand.utils.models.internet.{Url, DomainName, Http}

import scala_mash.highrise_api._
import models._
import enumerations.VisibleToValues._
import enumerations.NoteSubjectType
import enumerations.CollectionType

object NoteServicesSpec extends Specification {

	"The Notes services" should {

		setSequential()

		doBefore { 
			person = Person.create( person, account)
		}

		doAfter {
			Person.destroy(person, account)
		}

		"create a note on a person" in {
			val id: Long = person.id.getOrElse(0).asInstanceOf[Long]
			val note = Note( "Hello world", id, NoteSubjectType.Party)

			val createdNote = Note.create( note, account)

			createdNote.id must beSome[Long]

			createdNote.body must_==( note.body)
		}
		/*
		"create a note on a company" in {
		}
		"create a note on a deal" in {
		}
		"create a note on a case" in {
		}
		"get a note" in {
		}
		"update a note" in {
		}
		"list all for people" in {
		}
		"list all for companies" in {
		}
		"list all for cases" in {
		}
		"list all for deals" in {
		}
		"delete a note" in {
		}
		*/
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
}
