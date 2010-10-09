package specs
import org.specs.Specification
import org.specs.runner.JUnit4

import org.joda.time._

import scala.{List, Nil}

import scala_mash.highrise_api.models._
import CategoryTypes._

import scala_mash.rest.util.Helpers._

/**
 *
 * @author jimbarrows
 * @created: Dec 25, 2009 11:35:42 AM
 * @version 1.0
 *
 */

object CategorySpec extends Specification {

	val now = new DateTime(2010,01,28,13,13,13,0, DateTimeZone.forID("+00:00"))

	val filledTaskCategory = <task-category>
		<id type="integer">100</id>
		<name>Blech</name>
		<account-id>100</account-id>
		<created-at>{printWithTimeZone(now)}</created-at>
		<updated-at>{printWithTimeZone(now)}</updated-at>
		<elements-count type="integer">100</elements-count>
	</task-category>

	val filledDealCategory = <deal-category>
		<id type="integer">100</id>
		<name>Blech</name>
		<account-id>100</account-id>
		<created-at>{printWithTimeZone(now)}</created-at>
		<updated-at>{printWithTimeZone(now)}</updated-at>
		<elements-count type="integer">100</elements-count>
		</deal-category>

  "The task and deal category classes"  should {

		"be able to generate a task-category xml" in {
			TaskCategory(Some(100),
			"Blech",
			Some(100),
			Some(now),
			Some(now),
			Some(100)).toXml must equalIgnoreSpace (filledTaskCategory)
		}

		"be able to generate a deal-category xml" in {
			DealCategory(Some(100),
			"Blech",
			Some(100),
			Some(now),
			Some(now),
			Some(100)).toXml must equalIgnoreSpace (filledDealCategory)
		}

		"be able to parse a task-category xml" in {
			val expectedTaskCategory = TaskCategory(Some(100), "Blech", Some(100), Some(now), Some(now), Some(100))
			val actual = TaskCategory.parse(filledTaskCategory)
			 actual must_== (expectedTaskCategory )
		}

		"be able to parse a deal-category xml" in {
			DealCategory.parse(filledDealCategory) must be_==( DealCategory(Some(100),
				"Blech", Some(100), Some(now), Some(now), Some(100)))
		}
	}
}
