import models._
import models.RoomType._
import entities._
import org.joda.time.MonthDay
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.i18n.Lang
import play.api.test._
import play.api.test.Helpers._
import play.api.Play.current


@RunWith(classOf[JUnitRunner])
class MailSpec extends Specification {
	
	val fakeApplicationWithGlobal = FakeApplication()

	val email = "steiner.marcello@gmail.com"
	val person = Customer()
	val kid = Customer(11)

	"Mailer" should {
		"send an email for the user" in {
			val lang = Lang("en")
			val res = Mailer.send(email, (new MonthDay, new MonthDay), RoomType.lakeView, Treatment.bb, person, person, kid)

			res must equalTo(true)
		}
	}
	
}