package spec
import org.specs.Specification
import org.specs.runner.JUnit4

import org.joda.time.DateTime

import scala.{List, Nil}

import scala_mash.freshbooks_api._
import model._
import Utils._

import _root_.specs.Utils._

/**
 *
 * @author jimbarrows
 *
 */

object ClientServicesSpec extends Specification {

  "Client services"  should {
		val expectedClient = new Client(None, //clientId
			"Jane",  //firstName
			"Doe", //lastName
			"ABC Corp", //organization
			"janedoe@freshbooks.com", //email
			None, //username
			None,//password
			Some("(555) 123-4567"),//workPhone
			Some("(555) 234-5678"),//homePhone
			None,//mobile
			None,//fax
			None,//notes
			PrimaryAddress(Some("123 Fake St."),//pStreet1
			Some("Unit 555"),//pStreet2
			Some("New York"),//pCity
			Some("New York"),//pState
			Some("United States"),//pCountry
			Some("553132")),//pCode
			SecondaryAddress(None,//sStreet1
			None,//sStreet2
			None,//sCity
			None,//sState
			None,//sCountry
			None)//sCode
			)


		"be able to perform CRUD operations on a client" in {
			val actualCreated = Client.create( expectedClient, account)	
			actualCreated.clientId must beSome[Long]
			Client.get( actualCreated.clientId.get, account) must  beLike{case Client(
						actualCreated.clientId, 
						actualCreated.firstName, 
						actualCreated.lastName, 
						actualCreated.organization, 
						actualCreated.email, 
						_, 
						_, 
						actualCreated.workPhone, 
						actualCreated.homePhone, 
						actualCreated.mobile, 
						actualCreated.fax, 
						actualCreated.notes, 
						actualCreated.primaryAddress, 
						actualCreated.secondaryAddress) => true}
 
			Client.update( expectedClient, account)	
			Client.delete( actualCreated.clientId.get,account)
		}


	}
}
