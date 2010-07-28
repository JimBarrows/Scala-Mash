package test
import org.specs.Specification
import org.specs.runner.JUnit4
import scala_mash.highrise_api.Utils._
import _root_.org.joda.time._

class TestUtils extends JUnit4(UtilsSpec)
object UtilsSpec extends Specification {
	"A date parser" should {
		"be able to parse 2010-01-02T19:53:49Z" in {
			val expected = new DateTime(2010,01,02,19,53,49,0, DateTimeZone.forID("+00:00"))
			parseDateTimeWithTimeZone( "2010-01-02T19:53:49Z") must_== expected
		}
		"be able to format 2010-01-02T19:53:49Z" in {
			val expected = "2010-01-02T19:53:49Z"
			val date = new DateTime(2010,01,02,19,53,49,0, DateTimeZone.forID("+00:00"))
			printWithTimeZone(date) must_== expected
		}
	}
}
