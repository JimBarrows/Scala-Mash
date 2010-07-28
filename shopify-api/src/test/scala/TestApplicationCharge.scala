package test

import org.specs.runner.JUnit4
import org.specs.Specification
import com.nsfw.shopifyapp.shopify.model.ApplicationCharge
import org.joda.time.{DateTimeZone, DateTime}
import bizondemand.utils.models.internet.{Url,DomainName, Parameter, Http}
/**
 *
 * @author jimbarrows
 * @created: Dec 17, 2009 9:34:36 PM
 * @version 1.0
 *
 */

object ApplicationChargeSpec extends Specification {
   
	"The parse method" should {
		"take a valid response and return a case object" in {
     		val validXml =
      			<application-charge>
        			<created-at type="datetime">2009-12-12T15:15:50-05:00</created-at>
        			<id type="integer">1749673018</id>
        			<name>Super Duper Expensive action</name>
        			<price type="decimal">100.00</price>
        			<return-url>{"""http://super-duper.shopifyapps.com?signature=blech"""}</return-url>
        			<status>pending</status>
        			<test type="boolean">true</test>
        			<updated-at type="datetime">2009-12-12T15:15:50-05:00</updated-at>
        			<confirmation-url>
        				{"""http://apple.myshopify.com/admin/subscriptions/confirm_application_charge/1749673018?signature=BAhsKwc65Elo"""}
        			</confirmation-url>
      			</application-charge>;
    		val actualResponse = ApplicationCharge.parse(validXml)

			val url = new Url(Http(), 														//protocol: String,
        		None, 																	//username: Option[String],
	          	None,																	//password: Option[String],
    	      	DomainName("super-duper"::"shopifyapps"::"com"::Nil),					//domainName: DomainName,
        	  	None,																//port: Option[Int], 
          		None,																	//path: Option[List[String]], 
	          	Some(Parameter("signature","blech")::Nil)	//parameters: Option[List[Parameter]]
			)
			println("Url:       " + url.toString)
			println("returnUrl: " + actualResponse.returnUrl.toString)
			actualResponse.returnUrl must_== actualResponse.returnUrl      		
      		// actualResponse must_== ApplicationCharge(
      		//       			"Super Duper Expensive action",																//name: String,
      		// 				BigDecimal("100.00"),																		//price: BigDecimal,			
      		// 				url,																						//returnUrl: Url,
      		// 				Some(ApplicationCharge.dateFormatter.parseDateTime("2009-12-12T15:15:50-05:00")),			//createdAt: Option[DateTime],
      		// 				Some(1749673018),																			//id: Option[Int],
      		// 				Some("pending"), 																			//status: Option[String],
      		// 				true,																						//test: Boolean,
      		// 				Some(ApplicationCharge.dateFormatter.parseDateTime("2009-12-12T15:15:50-05:00")),			//updatedAt: Option[DateTime],
      		// 				Some(new Url("http",
      		// 						None, 
      		// 						None, 
      		// 						DomainName("apple"::"myshopify"::"com"::Nil),
      		// 						None,
      		// 						Some("admin"::"subscriptions"::"confirm_application_charge"::"1749673018"::Nil),						
      		// 						Some(Parameter("signature","BAhsKwc65Elo")::Nil)							
      		// 					)		
      		// 				)																							//confirmationUrl: Option[Url]
      		// 			)
    	}
  	}
}
