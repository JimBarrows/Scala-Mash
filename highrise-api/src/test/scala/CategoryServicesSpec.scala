package specs
import scala_mash.highrise_api._
import scala_mash.highrise_api.models._
import scala_mash.highrise_api.models.CategoryTypes._
import scala_mash.highrise_api.Account
import org.joda.time._
import org.specs.runner.JUnit4
import org.specs.Specification


object CategoryServicesSpec extends Specification {
	
	val account = Account("TestAccountForMe", "1ad2fc1adf9e7fc1f342d0e431069af0")

	val copywriting = DealCategory( Some(1660938), "Copywriting", Some(109504l), 
					Some(new DateTime(2010,01,02,19,53,49,0,DateTimeZone.forID("+00:00"))),
					Some(new DateTime(2010,01,02,19,53,49,0,DateTimeZone.forID("+00:00"))),Some(0))  
	val printProject = DealCategory( Some(1660935), "Print Project", Some(109504l), 
					Some(new DateTime(2010,01,02,19,53,49,0,DateTimeZone.forID("+00:00"))),
					Some(new DateTime(2010,01,02,19,53,49,0,DateTimeZone.forID("+00:00"))),Some(0))  
	val strategyConsulting = DealCategory( Some(1660937), "Strategy Consulting", Some(109504l), 
					Some(new DateTime(2010,01,02,19,53,49,0,DateTimeZone.forID("+00:00"))),
					Some(new DateTime(2010,01,02,19,53,49,0,DateTimeZone.forID("+00:00"))),Some(0)) 
	val uiDesign = DealCategory( Some(1660936), "UI Design", Some(109504l), 
					Some(new DateTime(2010,01,02,19,53,49,0,DateTimeZone.forID("+00:00"))),
					Some(new DateTime(2010,01,02,19,53,49,0,DateTimeZone.forID("+00:00"))),Some(0))  
	val webSiteDesign = DealCategory( Some(1660934), "Web Site Design", Some(109504l), 
					Some(new DateTime(2010,01,02,19,53,49,0,DateTimeZone.forID("+00:00"))),
					Some(new DateTime(2010,01,02,19,53,49,0,DateTimeZone.forID("+00:00"))),Some(0)) 

	val dealCategoryList =copywriting :: printProject :: strategyConsulting :: uiDesign :: webSiteDesign :: Nil


	val call = TaskCategory( Some(1660925),  "Call", Some(109504l), 
			Some(new DateTime(2010,01,02,19,53,49,0,DateTimeZone.forID("+00:00"))),
			Some(new DateTime(2010,01,02,19,53,49,0,DateTimeZone.forID("+00:00"))),Some(0))  
	val demo = TaskCategory( Some(1660926), "Demo", Some(109504l), 
			Some(new DateTime(2010,01,02,19,53,49,0,DateTimeZone.forID("+00:00"))),
			Some(new DateTime(2010,01,02,19,53,49,0,DateTimeZone.forID("+00:00"))),Some(0)) 
	val email = TaskCategory( Some(1660927), "Email", Some(109504l), 
			Some(new DateTime(2010,01,02,19,53,49,0,DateTimeZone.forID("+00:00"))),
			Some(new DateTime(2010,01,02,19,53,49,0,DateTimeZone.forID("+00:00"))),Some(0)) 
	val fax = TaskCategory( Some(1660928), "Fax", Some(109504l), 
			Some(new DateTime(2010,01,02,19,53,49,0,DateTimeZone.forID("+00:00"))),
			Some(new DateTime(2010,01,02,19,53,49,0,DateTimeZone.forID("+00:00"))),Some(0)) 
	val followUp = TaskCategory( Some(1660929), "Follow-up", Some(109504l), 
			Some(new DateTime(2010,01,02,19,53,49,0,DateTimeZone.forID("+00:00"))),
			Some(new DateTime(2010,01,02,19,53,49,0,DateTimeZone.forID("+00:00"))),Some(0)) 
	val lunch = TaskCategory( Some(1660930), "Lunch", Some(109504l), 
			Some(new DateTime(2010,01,02,19,53,49,0,DateTimeZone.forID("+00:00"))),
			Some(new DateTime(2010,01,02,19,53,49,0,DateTimeZone.forID("+00:00"))),Some(0))
	val meeting = TaskCategory( Some(1660931), "Meeting", Some(109504l), 
			Some(new DateTime(2010,01,02,19,53,49,0,DateTimeZone.forID("+00:00"))),
			Some(new DateTime(2010,01,02,19,53,49,0,DateTimeZone.forID("+00:00"))),Some(0))
	val ship = TaskCategory( Some(1660932), "Ship", Some(109504l), 
			Some(new DateTime(2010,01,02,19,53,49,0,DateTimeZone.forID("+00:00"))),
			Some(new DateTime(2010,01,02,19,53,49,0,DateTimeZone.forID("+00:00"))),Some(0)) 
	val thankYou = TaskCategory( Some(1660933), "Thank-you", Some(109504l), 
			Some(new DateTime(2010,01,02,19,53,49,0,DateTimeZone.forID("+00:00"))),
			Some(new DateTime(2010,01,02,19,53,49,0,DateTimeZone.forID("+00:00"))),Some(0))

	val taskCategoryList =  call :: demo :: email :: fax :: followUp :: lunch :: meeting :: ship :: thankYou :: Nil

	"Task and deal category services" should {

		"be able get a single task category" in {
			TaskCategory.show( call.id.get, account) must be_== ( call)
			TaskCategory.show( demo.id.get, account) must be_== ( demo)
			TaskCategory.show( email.id.get, account) must be_== ( email)
			TaskCategory.show( fax.id.get, account) must be_== ( fax)
			TaskCategory.show( followUp.id.get, account) must be_== ( followUp)
			TaskCategory.show( lunch.id.get, account) must be_== ( lunch)
			TaskCategory.show( meeting.id.get, account) must be_== ( meeting)
			TaskCategory.show( ship.id.get, account) must be_== ( ship)
			TaskCategory.show( thankYou.id.get, account) must be_== ( thankYou)
		}

		"be able get a single deal category" in {
			DealCategory.show( webSiteDesign.id.get, account) must be_== ( webSiteDesign)
															
		}

		"be able get a list of deal categories" in {
			DealCategory.list( account) must be_== ( dealCategoryList)
		}



		"be able get a list of task categories" in {
			TaskCategory.list(account) must be_== ( taskCategoryList)
		}


		"be able to create update and delete a task category" in {

			// var taskCategory:TaskCategory = TaskCategory.create( "test", account) 
			// 			taskCategory.name must be_==("test")
			// 
			// 			taskCategory.name = "Modified Test Name"	
			// 			TaskCategory.update( taskCategory, account) 
			// 			val actual = TaskCategory.show( taskCategory.id.getOrElse(0), account)
			// 			actual.id must be_==( taskCategory.id)
			// 			actual.name must be_==(taskCategory.name)
			// 			actual.accountId must be_==(taskCategory.accountId)
			// 			actual.createdAt must be_==(taskCategory.createdAt)
			// 			actual.elementsCount must be_==(taskCategory.elementsCount)
			// 
			// 			TaskCategory.destroy( taskCategory, account)
			// 
			// 			TaskCategory.list(account) must be_== ( taskCategoryList)
		}

		"be able to create, update and delete a deal category" in {
			// var dealCategory:DealCategory = DealCategory.create( "test", account) 
			// 			dealCategory.name must be_==("test")
			// 
			// 			dealCategory.name = "Modified Test Name"	
			// 			DealCategory.update( dealCategory, account) 
			// 			val actual = DealCategory.show( dealCategory.id.getOrElse(0), account)
			// 			actual.id must be_==( dealCategory.id)
			// 			actual.name must be_==(dealCategory.name)
			// 			actual.accountId must be_==(dealCategory.accountId)
			// 			actual.createdAt must be_==(dealCategory.createdAt)
			// 			actual.elementsCount must be_==(dealCategory.elementsCount)
			// 
			// 			DealCategory.destroy( dealCategory, account)
			// 
			// 			DealCategory.list(account) must be_== ( dealCategoryList)
		}
	}
}
