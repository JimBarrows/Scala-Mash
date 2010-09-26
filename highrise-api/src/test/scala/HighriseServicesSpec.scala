package specs
import scala_mash.highrise_api.{Account, HighriseServices, LoginFailed}
import scala_mash.highrise_api.models._
import org.specs.runner.JUnit4
import org.specs.Specification
import scala_mash.highrise_api.models.enumerations.VisibleToValues._

/**
 * 
 *
 */

object HighriseServicesSpec extends Specification {
				
	object HRS extends HighriseServices[Person]

  "Highrise Services" should {
    "be able to get an siteName and apiKey from system properties" in {
      System.setProperty("com.nsfw.highrisehq.apiKey", "123456")
      System.setProperty("com.nsfw.highrisehq.siteName", "TestAccountName")
      HRS.apiKey must be_== ("123456")
      HRS.siteName must be_== ("TestAccountName")
    }
    "have a default siteName and apiKey that should be provided if one is not set in system properties" in {
      System.clearProperty("com.nsfw.highrisehq.siteName")
      System.clearProperty("com.nsfw.highrisehq.apiKey")
      HRS.apiKey must be_== ("some_apiKey")
      HRS.siteName must be_== ("some_account")
    }
    "provide an account object generated from the system properties" in {
      System.setProperty("com.nsfw.highrisehq.apiKey", "123456")
      System.setProperty("com.nsfw.highrisehq.siteName", "TestAccountName")
      HRS.account must beLike{ case Account("TestAccountName", "123456") => true}
    }
    "Bug #248 - Highrise redirects unkown Domain Names to front page, service should throw an error." in {
    	val expectedContactData = ContactData(None, None, None, None, None)
      	val person = Person(None,
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
	    val account = new Account("DoesntExistAnywhere", "12345")
	    
    	Person.create(person, account) must throwA[LoginFailed]
    }
  }
}
