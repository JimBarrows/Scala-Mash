import org.specs.Specification
import org.specs.runner.JUnit4

import org.joda.time.DateTime

import scala.{List, Nil}

import com.bizondemand.freshbooks_api.model._
import com.bizondemand.freshbooks_api.Utils._
/**
 *
 * @author jimbarrows
 *
 */

class TestClient extends JUnit4(ClientSpec)
object ClientSpec extends Specification {

  "The Client class"  should {

		val expectedXml = <client>
    <first_name>Jane</first_name>
    <last_name>Doe</last_name>
    <organization>ABC Corp</organization>
    <email>janedoe@freshbooks.com</email>
    <username>janedoe</username>            
    <password>seCret!7</password>           
    <work_phone>(555) 123-4567</work_phone> 
    <home_phone>(555) 234-5678</home_phone>
    <mobile></mobile>                      
    <fax></fax>                           
    <notes></notes>                       
    <p_street1>123 Fake St.</p_street1>
    <p_street2>Unit 555</p_street2>
    <p_city>New York</p_city>
    <p_state>New York</p_state>
    <p_country>United States</p_country>
    <p_code>553132</p_code>
    <s_street1></s_street1>
    <s_street2></s_street2>
    <s_city></s_city>
    <s_state></s_state>
    <s_country></s_country>
    <s_code></s_code>
  </client>

	val expectedClient = Client(None, //clientId
			"Jane",  //firstName
			"Doe", //lastName
			"ABC Corp", //organization
			"janedoe@freshbooks.com", //email
			Some("janedoe"), //username
			Some("seCret!7"),//password
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

		val getResponseXml = <response status="ok" xmlns="http://www.freshbooks.com/api/" >
			<client>
				<first_name>Jane</first_name>
				<last_name>Doe</last_name>
				<organization>ABC Corp</organization>
				<email>janedoe@freshbooks.com</email>
				<username>janedoe</username>            
				<password>seCret!7</password>           
				<work_phone>(555) 123-4567</work_phone> 
				<home_phone>(555) 234-5678</home_phone>
				<mobile></mobile>                      
				<fax></fax>                           
				<notes></notes>                       
				<p_street1>123 Fake St.</p_street1>
				<p_street2>Unit 555</p_street2>
				<p_city>New York</p_city>
				<p_state>New York</p_state>
				<p_country>United States</p_country>
				<p_code>553132</p_code>
				<s_street1></s_street1>
				<s_street2></s_street2>
				<s_city></s_city>
				<s_state></s_state>
				<s_country></s_country>
				<s_code></s_code>
			</client>
		</response>


		
		"be able to generate a client xml" in {
			expectedClient.toXml must equalIgnoreSpace (expectedXml)
		}

		"be able to parse a response from a get request"  in {
				Client.parse(getResponseXml) must be_== (expectedClient)
		}

		"be able to parse a response from a create request" in {
				val parsed = Client.parseCreateResponse(<response xmlns="http://www.freshbooks.com/api/" status="ok"><client_id>13</client_id></response>)
				parsed.clientId must beSome[Long]
		}
	}
}
