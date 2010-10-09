package specs
import org.specs.Specification
import org.specs.runner.JUnit4

import org.joda.time.DateTime

import scala.{List, Nil}

import scala_mash.highrise_api.models._
import scala_mash.highrise_api.Account

import scala_mash.highrise_api.models._
import SubjectType.{Person => PersonSubject}
import scala_mash.highrise_api.models.enumerations.VisibleToValues._
/**
 *
 * @author jimbarrows
 * @created: Feb 13, 2009
 * @version 1.0
 *
 */

object TagServicesSpec extends Specification {
	val account = Account("TestAccountForMe", "1ad2fc1adf9e7fc1f342d0e431069af0")

	"Tag services" should {
		
		"Add a tag, Read, and remove a Tag from a person" in {
			 val createdPerson = Person.create(person, account)
			 val newTag = Tag(None, "TestTagServices")
			 val createdTag = Tag.addTag(
			 	createdPerson, //person:Person, 
			 	newTag, //tag:Tag, 
			 	account //account:Account
			 )
			 
			 createdTag.name must be_== (newTag.name)
			 
		}
		
		"be able to list all tags" in {
			//Tag.listAll(account) must be_== ( Tag(Some(800982), "Test Contact") :: Nil)
		}		

		"be able to list all tags for a person" in {
			//Tag.listTagsForAPerson( person, account) must be_== ( Tag(Some(800982), "Test Contact") :: Nil)
		}
	}
	
	val person = Person( None,	//id: Option[Long],
			"Test", 					//firstName: String,
            "Contact",			      	//lastName: String,
			"",							//title: String,
			Some("TestTagServices"),							//background: String,
			None,						//companyId: Option[Int],
			None,	//createdAt: Option[DateTime],
        	None, 	//updatedAt: Option[DateTime],
            Some(Everyone),									//visibleTo: Option[VisibleToValues],
            None,										//ownerId: Option[Int],
            None,										//groupId: Option[Int],
        	None,								//authorId: Option[Int],
        	ContactData(None, None, None, None, None))	//contactData: ContactData

}
