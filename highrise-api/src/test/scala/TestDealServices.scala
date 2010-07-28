package test
import scala_mash.highrise_api.models._
import scala_mash.highrise_api.Account
import org.joda.time.DateTime
import org.specs.runner.JUnit4
import org.specs.Specification
import scala_mash.highrise_api.models.enumerations._
import VisibleToValues._
import DealStatus._
import scala_mash.rest.OtherServerError
import scala_mash.rest.RestException

object DealServicesSpec extends Specification {
	
	val account = Account("TestAccountForMe", "1ad2fc1adf9e7fc1f342d0e431069af0")
	
	"Deal Services" should {
		"Create, read update and delete a deal" in {
			
			val response = Deal.create( newDeal, account)
			response.name must equalTo( "Create Test Deal")
			response.createdAt must beSomething
			response.id must beSomething
			
			val showResponse = Deal.show( response.id.get, account)
			response must_== response
			
			val modifiedDeal = Deal( response.accountId, 	//accountId : Option[Long], 
				response.authorId, 							//authorId: Option[Long], 
				Some("Changed background."), 				//background: Option[String],
				response.categoryId, 						//categoryId : Option[Long],
				response.createdAt, 						//createdAt : Option[DateTime],
				response.currency, 							//currency : Option[String],
				response.duration, 							//duration : Option[Long],
				response.groupId, 							//groupId : Option[Long],
				response.id, 								//id : Option[Long],
				response.name, 								//name : String,
				response.ownerId, 							//ownerId : Option[Long],
				response.partyId, 							//partyId : Option[Long],
				response.price, 							//price :Option[Long],
				response.priceType, 						//priceType:Option[String],
				response.responsiblePartyId, 				//responsiblePartyId:Option[Long],
				response.status, 							//status:Option[DealStatus], 
				response.statusChangedOn, 					//statusChangedOn: Option[LocalDate],
				response.updatedAt, 						//updatedAt:Option[DateTime],
				response.visibleTo, 						//visibleTo:Option[VisibleToValues],
				response.dealCategory						//dealCategory:Option[DealCategory]
				)	
							
			Deal.update( modifiedDeal, account)			
			
			Deal.destroy( modifiedDeal.id.get, account)
			
		}
		
		"get all deals" in {
			
			val response = Deal.create( newDeal, account)
			
			val list = Deal.listAll( account)
			list must haveSize( 1 )
			
			val first = list.head
			first.name must equalTo( "Create Test Deal")
			first.createdAt must beSomething
			first.id must beSomething
			
			Deal.destroy( response.id.get, account)
		}		
		
		"update the status of a deal" in {
			try {
				val response = Deal.create( newDeal, account)
				Deal.statusUpdate(response.id.get, Won, account)
				Deal.destroy( response.id.get, account)
			} catch {
				case e : RestException => {
					//e.httpStatus must be_==(OtherServerException(507, _))
				}
				case e =>{
					e.printStackTrace
					fail("Exception was thrown")
				}
			}
		}
	}
	
	val newDeal = Deal( None, //accountId : Option[Long], 
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
				None //dealCategory:Option[DealCategory]
				)	
}