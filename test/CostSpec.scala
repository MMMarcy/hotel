import models._
import models.PriceObject._
import org.joda.time.MonthDay
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._


/**
 * add your integration spec here.
 * An integration test will fire up a whole play application in a real (or headless) browser
 */
@RunWith(classOf[JUnitRunner])
class CostSpec extends Specification {

  "PriceObject" should {

    "calculate the right price for a single adult" in  {

     PriceObject.getPriceForAdult((new MonthDay(3,1), new MonthDay(3,5)),RoomType.lakeView, Treatment.fb) must equalTo( List[Float](61.0F, 61.0F, 61.0F, 61.0F, 61.0F))
    }
  }
}

