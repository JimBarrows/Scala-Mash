package specs
import scala_mash.highrise_api.models._
import scala_mash.highrise_api.{Account, LoginFailed}
import org.joda.time.DateTime
import org.specs.runner.JUnit4
import org.specs.Specification
import scala_mash.highrise_api.models.enumerations._
import VisibleToValues._

object PersonServicesSpec extends Specification {

 	val account = Account("testaccountforme", "1ad2fc1adf9e7fc1f342d0e431069af0")

	"Person Services" should {
    	"be able to create a person with no contact data" in {
	      	val expectedContactData = ContactData(None, None, None, None, None)
	      	val expectedPerson = Person(None,
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
		    val actualPerson: Person = Person.create(expectedPerson, account)
		    actualPerson.id must beSome[Long].which(_ > 0)
		    actualPerson.createdAt must beSome[DateTime]
		    actualPerson.updatedAt must beSome[DateTime]
		    actualPerson.firstName must be_==(expectedPerson.firstName)
		    actualPerson.lastName must be_==(expectedPerson.lastName)
		    actualPerson.title must be_==(expectedPerson.title)
		    actualPerson.background must be_==(expectedPerson.background)
		    actualPerson.companyId must beNone
		    actualPerson.visibleTo must beSome[VisibleToValues].which(_ == Everyone)
		    actualPerson.ownerId must beNone
		    actualPerson.groupId must beNone
		    actualPerson.authorId must beSome[Int].which(_ > 0)
		    actualPerson.contactData must be_==(expectedContactData)
    	}

    	"be able to create a person with one email address" in {
    		import AddressLocationValues._
    		val expectedContactData = ContactData(Some(EmailAddress(None, "someone@somewhere.com", Home) :: Nil), None, None, None, None)
    		val expectedPerson = Person(None,
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
        		expectedContactData)
      		val actualPerson: Person = Person.create(expectedPerson, account)
      		actualPerson.id must beSome[Long].which(_ > 0)
      		actualPerson.createdAt must beSome[DateTime]
		      actualPerson.updatedAt must beSome[DateTime]
		      actualPerson.firstName must be_==(expectedPerson.firstName)
		      actualPerson.lastName must be_==(expectedPerson.lastName)
		      actualPerson.title must be_==(expectedPerson.title)
		      actualPerson.background must be_==(expectedPerson.background)
		      actualPerson.companyId must beNone
		      actualPerson.visibleTo must beSome[VisibleToValues].which(_ == Everyone)
		      actualPerson.ownerId must beNone
		      actualPerson.groupId must beNone
		      actualPerson.authorId must beSome[Int].which(_ > 0)
		      actualPerson.contactData.emailAddresses must beSome[List[EmailAddress]]
		      actualPerson.contactData.emailAddresses.get.first.address must be_==("someone@somewhere.com")
		      actualPerson.contactData.emailAddresses.get.first.location must be_==(Home)
    	}

	    "be able to create a person with one phone number" in {
		    val expectedContactData = ContactData(None, Some(PhoneNumber(None, "555-555-12345", scala_mash.highrise_api.models.PhoneLocationValues.Home) :: Nil), None, None, None)
		    val expectedPerson = Person(None,
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
		        expectedContactData)
		    val actualPerson: Person = Person.create(expectedPerson, account)
		    actualPerson.id must beSome[Long].which(_ > 0)
		    actualPerson.createdAt must beSome[DateTime]
		    actualPerson.updatedAt must beSome[DateTime]
		    actualPerson.firstName must be_==(expectedPerson.firstName)
		    actualPerson.lastName must be_==(expectedPerson.lastName)
		    actualPerson.title must be_==(expectedPerson.title)
		    actualPerson.background must be_==(expectedPerson.background)
		    actualPerson.companyId must beNone
		    actualPerson.visibleTo must beSome[VisibleToValues].which(_ == Everyone)
		    actualPerson.ownerId must beNone
		    actualPerson.groupId must beNone
		    actualPerson.authorId must beSome[Int].which(_ > 0)
		    actualPerson.contactData.phoneNumbers must beSome[List[PhoneNumber]]
		    actualPerson.contactData.phoneNumbers.get.first.number must be_==("555-555-12345")
		    actualPerson.contactData.phoneNumbers.get.first.location must be_==(scala_mash.highrise_api.models.PhoneLocationValues.Home)
	    }
	
	 	"be able to create a person with one address" in {
		    val expectedContactData = ContactData(
		    None,
		    None,
		    Some(
		    	Address(
			        None,
			        "phoenix",
			        "usa",
			        "az",
			        "111 N somewhere",
			        "12345",
			        scala_mash.highrise_api.models.AddressLocationValues.Home
		        ) :: 
		        Nil
		    ),
		    None,
		    None)
		    val expectedPerson = Person(None,
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
			    expectedContactData)
		    val actualPerson: Person = Person.create(expectedPerson, account)
		    actualPerson.id must beSome[Long].which(_ > 0)
		    actualPerson.createdAt must beSome[DateTime]
		    actualPerson.updatedAt must beSome[DateTime]
		    actualPerson.firstName must be_==(expectedPerson.firstName)
		    actualPerson.lastName must be_==(expectedPerson.lastName)
		    actualPerson.title must be_==(expectedPerson.title)
		    actualPerson.background must be_==(expectedPerson.background)
		    actualPerson.companyId must beNone
		    actualPerson.visibleTo must beSome[VisibleToValues].which(_ == Everyone)
		    actualPerson.ownerId must beNone
		    actualPerson.groupId must beNone
		    actualPerson.authorId must beSome[Int].which(_ > 0)
		    actualPerson.contactData.addresses must beSome[List[Address]]
		    actualPerson.contactData.addresses.get.first.city must be_==("phoenix")
		    actualPerson.contactData.addresses.get.first.state must be_==("az")
		    actualPerson.contactData.addresses.get.first.street must be_==("111 N somewhere")
		    actualPerson.contactData.addresses.get.first.zip must be_==("12345")
		    actualPerson.contactData.addresses.get.first.location must be_==(scala_mash.highrise_api.models.AddressLocationValues.Home)
		}

		"be able to create a person with one instant messenger" in {
			val expectedContactData = ContactData(
      			None,
    			None,
    			None,
    			Some(
    				InstantMessenger(
    					None,
        				"username",
        				InstantMessengerProtocolValues.Other,
        				InstantMessengerLocationValues.Other
        				)  :: 
        			Nil
        		),
    			None
    		)
    		val expectedPerson = Person(None,
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
		    val actualPerson: Person = Person.create(expectedPerson, account)
		    actualPerson.id must beSome[Long].which(_ > 0)
		    actualPerson.createdAt must beSome[DateTime]
		    actualPerson.updatedAt must beSome[DateTime]
		    actualPerson.firstName must be_==(expectedPerson.firstName)
		    actualPerson.lastName must be_==(expectedPerson.lastName)
		    actualPerson.title must be_==(expectedPerson.title)
		    actualPerson.background must be_==(expectedPerson.background)
		    actualPerson.companyId must beNone
		    actualPerson.visibleTo must beSome[VisibleToValues].which(_ == Everyone)
		    actualPerson.ownerId must beNone
		    actualPerson.groupId must beNone
		    actualPerson.authorId must beSome[Int].which(_ > 0)
		    actualPerson.contactData.instantMessengers must beSome[List[InstantMessenger]]
		    actualPerson.contactData.instantMessengers.get.first.address must be_==("username")
		    actualPerson.contactData.instantMessengers.get.first.protocol must be_==(InstantMessengerProtocolValues.Other)
		    actualPerson.contactData.instantMessengers.get.first.location must be_==(InstantMessengerLocationValues.Other)
		}

		"be able to create a person with one web address" in {
	    	val expectedContactData = ContactData(
	      		None,
	    		None,
	    		None,
	    		None,
	    		Some(
	    			WebAddress(
	    				None,
	        			"http://somewhere.com",
	        			InstantMessengerLocationValues.Other
	        		):: 
	        		Nil
	              )
	    	)
	    	
	    	val expectedPerson = Person(None,
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
	     		expectedContactData)
	    	val actualPerson: Person = Person.create(expectedPerson, account)
		    actualPerson.id must beSome[Long].which(_ > 0)
		    actualPerson.createdAt must beSome[DateTime]
		    actualPerson.updatedAt must beSome[DateTime]
		    actualPerson.firstName must be_==(expectedPerson.firstName)
		    actualPerson.lastName must be_==(expectedPerson.lastName)
		    actualPerson.title must be_==(expectedPerson.title)
		    actualPerson.background must be_==(expectedPerson.background)
		    actualPerson.companyId must beNone
		    actualPerson.visibleTo must beSome[VisibleToValues].which(_ == Everyone)
		    actualPerson.ownerId must beNone
		    actualPerson.groupId must beNone
		    actualPerson.authorId must beSome[Int].which(_ > 0)
		    actualPerson.contactData.webAddresses must beSome[List[WebAddress]]
		    actualPerson.contactData.webAddresses.get.first.url must be_==("http://somewhere.com")
		    actualPerson.contactData.webAddresses.get.first.location must be_==(InstantMessengerLocationValues.Other)
	  	}

		"be able to find a single person by email" in {
	    	val expectedPerson = Person(None,
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
				ContactData(
					Some(
						EmailAddress(
							None, //id
							"Joe.Test@testEmail.com", //address
							AddressLocationValues.Other //location
						)::
						Nil
					),
					None,
					None,
					None,
					Some(
						WebAddress(
							None,
							"http://somewhere.com",
							InstantMessengerLocationValues.Other
						) :: 
						Nil
					)
				)
			)
	    	val actualPerson: Person = Person.create(expectedPerson, account)
			val found = Person.findPeopleByEmail(expectedPerson.contactData.emailAddresses.get.first.address, account)
		    actualPerson.id must beSome[Long].which(_ > 0)
		    actualPerson.createdAt must beSome[DateTime]
		    actualPerson.updatedAt must beSome[DateTime]
		    actualPerson.firstName must be_==(expectedPerson.firstName)
		    actualPerson.lastName must be_==(expectedPerson.lastName)
		    actualPerson.title must be_==(expectedPerson.title)
		    actualPerson.background must be_==(expectedPerson.background)
		    actualPerson.companyId must beNone
		    actualPerson.visibleTo must beSome[VisibleToValues].which(_ == Everyone)
		    actualPerson.ownerId must beNone
		    actualPerson.groupId must beNone
		    actualPerson.authorId must beSome[Int].which(_ > 0)
		    actualPerson.contactData.webAddresses must beSome[List[WebAddress]]
		    actualPerson.contactData.webAddresses.get.first.url must be_==("http://somewhere.com")
		    actualPerson.contactData.webAddresses.get.first.location must be_==(InstantMessengerLocationValues.Other)
		}

		"A LoginFailed exception should be thrown when a bad highrise sitename is used to find by email" in {
			val expectedPerson = Person(None,
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
				ContactData(
					Some(
						EmailAddress(
							None, //id
							"Joe.Test@testEmail.com", //address
							AddressLocationValues.Other //location
						)::
						Nil
					),
					None,
					None,
					None,
					Some(
						WebAddress(
							None,
							"http://somewhere.com",
							InstantMessengerLocationValues.Other
						) :: 
						Nil
					)
				)
				)
	    	val actualPerson: Person = Person.create(expectedPerson, account)
 				val badAccount = Account("badSiteNamexxx", "1ad2fc1adf9e7fc1f342d0e431069af0")
				Person.findPeopleByEmail(expectedPerson.contactData.emailAddresses.get.first.address, badAccount) must throwA[LoginFailed]
		}

  
//	    "be able to update a person with no contact data" in {
	    	// val startingData = ContactData(None, None, None, None, None)
	    	// 	    	var startingPerson = Person(None,
	    	// 				"Joe", //first name
	    	// 	        	"Tester", //last name
	    	// 	        	"JoeTester Title", //title
	    	// 	        	"update no contact data", //backgroun
	    	// 	        	None, //companyId
	    	// 	        	None, //createAt
	    	// 	        	None, //updatedAt
	    	// 	        	Some(Everyone), //visibleTo
	    	// 	        	None, //ownerId
	    	// 	        	None, //groupId
	    	// 	        	None, //authorId
	    	// 	        	startingData)
	    	// 	        println("Before create: " + startingPerson.toString)
	    	// 	      	startingPerson = Person.create(startingPerson, account)
	    	// 	      	println("after create: " + startingPerson.toString)
	    	// 			val expectedPerson = Person(startingPerson.id,
	    	// 				"Jane",
	    	// 				startingPerson.lastName,
	    	// 				startingPerson.title,
	    	// 				startingPerson.background,
	    	// 				startingPerson.companyId,
	    	// 				startingPerson.createdAt,
	    	// 				startingPerson.updatedAt,
	    	// 				startingPerson.visibleTo,
	    	// 				startingPerson.ownerId,
	    	// 				startingPerson.groupId,
	    	// 				startingPerson.authorId,
	    	// 				startingPerson.contactData)
	    	// 			Person.update( expectedPerson, account)
	    	// 			val actualPerson = Person.show( expectedPerson.id.get, account)
	    	// 			println("after update: " + actualPerson.toString)
	    	// 	      	actualPerson.id must beSome[Long].which(_ > 0)
	    	// 	      	actualPerson.createdAt must beSome[DateTime]
	    	// 	      	actualPerson.updatedAt must beSome[DateTime]
	    	// 	      	actualPerson.firstName must be_==(expectedPerson.firstName)
	    	// 	      	actualPerson.lastName must be_==(expectedPerson.lastName)
	    	// 	      	actualPerson.title must be_==(expectedPerson.title)
	    	// 	      	actualPerson.background must be_==(expectedPerson.background)
	    	// 	      	actualPerson.companyId must beNone
	    	// 	      	actualPerson.visibleTo must beSome[VisibleToValues].which(_ == Everyone)
	    	// 	      	actualPerson.ownerId must beNone
	    	// 	      	actualPerson.groupId must beNone
	    	// 	      	actualPerson.authorId must beSome[Int].which(_ > 0)
	    	// 	      	actualPerson.contactData must be_==(expectedPerson.contactData)
//	    }
    }
}
