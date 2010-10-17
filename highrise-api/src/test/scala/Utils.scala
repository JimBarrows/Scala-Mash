package specs


import scala_mash.highrise_api._
import models._
import enumerations.VisibleToValues._

object Utils {

	lazy val account = Account("TestAccountForMe", "1ad2fc1adf9e7fc1f342d0e431069af0")

	def createPerson(firstName: String, lastName: String, title: String, background: String) = 	Person(None,
			firstName, //first name
			lastName, //last name
			title, //title
			Some(background), //backgroun
			None, //companyId
			None, //createAt
			None, //updatedAt
			Some(Everyone), //visibleTo
			None, //ownerId
			None, //groupId
			None, //authorId
			createExpectedContactData
	)

	def createExpectedContactData = ContactData(None, None, None, None, None)

	def createCompany( name: String, background: String) = Company(
			None, //id
			name,  //name
			Some(background),  //background
			None, //createdAt
			None, //updateAt
			Some(Everyone), //visible to
			None,  //owner
			None,  //group
			None,  //author
			createExpectedContactData
    )

	def createDeal(name: String) = Deal( None, //accountId : Option[Long], 
			None, //authorId: Option[Long], 
			None, //background: Option[String],
			None, //categoryId : Option[Long],
			None, //createdAt : Option[DateTime],
			None, //currency : Option[String],
			None, //duration : Option[Long],
			None, //groupId : Option[Long],
			None, //id : Option[Long],
			name, //name : String,
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
