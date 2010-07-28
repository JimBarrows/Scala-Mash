package test
import com.nsfw.highrise.{Account, HighriseServices}
import com.nsfw.highrise.models._
import org.specs.runner.JUnit4
import org.specs.Specification

/**
 *
 * @author jimbarrows
 * @created: Dec 31, 2009 3:38:35 PM
 * @version 1.0
 *
 */

class TestHighriseServices extends JUnit4(HighriseServicesSpec)
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
  }
}
