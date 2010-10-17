package specs
import org.specs.Specification
import org.specs.runner.JUnit4

import org.joda.time.DateTime

import scala.{List, Nil}

import scala_mash.highrise_api.Account

import scala_mash.highrise_api.models._
import scala_mash.highrise_api.models.enumerations.VisibleToValues._

import specs.Utils._

/**
 *
 * @author jimbarrows
 * @created: Feb 13, 2009
 * @version 1.0
 *
 */

object TagServicesSpec extends Specification {

	"Tag services" should {

		shareVariables
		setSequential

		var personTag: Option[Tag] = None
		var companyTag: Option[Tag] = None
		var dealTag: Option[Tag] = None

		doFirst {
			person =  Person.create( person, account)
			company = Company.create( company, account)
			deal = Deal.create( deal, account)
		}

		doLast {
			Person.destroy(person, account)
			Company.destroy(company, account)
			Deal.destroy( deal, account)
		}

		
		"add a tag to a person" in {
			val created = Tag.addTagTo( person, Tag(None, "Person Test Tag"), account)
			created.id must beSome[Long]
			created.name must_== ("Person Test Tag")
			personTag = Some( created)
		}

		"list all tags for a person" in {
			personTag.map( tag => {
					val list = Tag.listTagsFor( person, account)
					list.head must be_== (tag)
				}
			)
		}


		"add a tag to a company" in {
			val created = Tag.addTagTo( company, Tag(None, "Company Test Tag"), account)
			created.id must beSome[Long]
			created.name must_== ("Company Test Tag")
			companyTag = Some( created)
		}

		"list all tags for a company" in {
			companyTag.map( tag => {
					val list = Tag.listTagsFor( company, account)
					list.head must be_== (tag)
				}
			)
		}


		"add a tag to a deal" in {
			val created = Tag.addTagTo( deal, Tag(None, "Deal Test Tag"), account)
			created.id must beSome[Long]
			created.name must_== ("Deal Test Tag")
			dealTag = Some( created)
		}

		"list all tags for a deal" in {
			dealTag.map( tag => {
					val list = Tag.listTagsFor( deal, account)
					list.head must be_== (tag)
				}
			)
		}

		"add a tag to a case" in {
		}

		"list all tags for a case" in {
		}

		"list all tags available" in {
			personTag.map( person => {
					dealTag.map( deal => {
							companyTag.map( company => {
									val list = Tag.listAll( account)
									list must contain( person)
									list must contain( deal)
									list must contain( company)
								}
							)
							}
						)
						}
					)
		}		

		"list all parties associated with a tag" in {
		}

		"remove a tag on a person" in {
			personTag.map( tag => {
					Tag.removeTag( person, tag, account)
					Tag.listTagsFor( person, account) must notContain( tag)
				}
			)
		}

		"remove a tag on a deal" in {
			dealTag.map( tag => {
					Tag.removeTag( deal, tag, account)
					Tag.listTagsFor( deal, account) must notContain( tag)
				}
			)
		}

		"remove a tag on a company" in {
			companyTag.map( tag => {
					Tag.removeTag( company, tag, account)
					Tag.listTagsFor( company, account) must notContain( tag)
				}
			)
		}

		"remove a tag on a case" in {
		}
	}
	
	var person = createPerson( "John", "Test", "Test Guy", "Semi random test data")

	var company = createCompany( "Test Company", "Random test data")

	var deal = createDeal("Test Deal")
}
