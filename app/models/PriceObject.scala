package models

import org.joda.time.MonthDay
import play.api.cache.Cache
import play.api.Play.current

object RoomType extends Enumeration {
  type RoomType = Value
  val lakeView, mountainView, suite, single = Value
}

object Treatment extends Enumeration {
  type Treatment = Value
  val bb, hb, fb = Value
}

object PriceObject {

  import RoomType._
  import Treatment._

  val PRICESTRING = "formattedPrices"

  val A = Period((new MonthDay(1, 10), new MonthDay(4, 8)), (new MonthDay(10, 7), new MonthDay(12, 5)))
  val B = Period((new MonthDay(4, 28), new MonthDay(6, 2)))
  val C = Period((new MonthDay(4, 9), new MonthDay(4, 27)), (new MonthDay(12, 6), new MonthDay(12, 21)))
  val D = Period((new MonthDay(6, 3), new MonthDay(7, 6)), (new MonthDay(9, 15), new MonthDay(10, 6)))
  val E = Period((new MonthDay(7, 7), new MonthDay(9, 14)), (new MonthDay(12, 22), new MonthDay(12, 31)), (new MonthDay(1, 1), new MonthDay(1, 6)))
  val periods = List[Period](A, B, C, D, E)

  def printPeriods = {
    val strbld = new StringBuilder()
    periods.foreach{ period =>
      strbld.append("""<div class="col-xs-2 text-center val-per">""")
      period.intervals.foreach{ interval =>
        strbld.append(interval._1.toString("dd/MM"))
        strbld.append(" - ")
        strbld.append(interval._2.toString("dd/MM"))
        strbld.append("""<br/>""")
      }
      strbld.append("""</div>""")
    }
    strbld.toString
  }

  def printRow(room: RoomType, treatment: Treatment): String = {
    Cache.getAs[String](PRICESTRING+room.toString+treatment.toString) match {
      case Some(str) => str
      case None => {
        val strbld = new StringBuilder()
        var partialFun = map(room)(treatment)(_)
        periods.foreach(period => {
          val day = period.intervals.head._1
          strbld.append("""<div class="col-xs-2 val text-center">""")
          strbld.append(UtilObject.format(partialFun(day)))
          strbld.append("</div>")
        })
        val res = strbld.toString()
        Cache.set(PRICESTRING+room.toString+treatment.toString, res, 43200)
        res
      }
    }

  }

  def getPriceForAdult(interval: (MonthDay, MonthDay), room: RoomType, treatment: Treatment): List[Float] = {
    @annotation.tailrec
    def innerFun(interval: (MonthDay, MonthDay), acc: List[MonthDay]): List[MonthDay] = {
      interval match {
        case (d1, d2) if d1.isBefore(d2) || d1.isEqual(d2) => innerFun((d1.plusDays(1), d2), d1 :: acc)
        case _ => acc
      }
    }
    val partialFun = map(room)(treatment)(_)
    innerFun(interval, List[MonthDay]()).map(a => partialFun(a))
  }

  def getPriceForChildren(age: Int, interval: (MonthDay, MonthDay), room: RoomType, treatment: Treatment): List[Float] = {
    if (age <= 2) getPriceForAdult(interval, room, treatment).map(_ * 0F)
    else if (age <= 6) getPriceForAdult(interval, room, treatment).map(_ * 0.5F)
    else if (age <= 11) getPriceForAdult(interval, room, treatment).map(_ * 0.8F)
    else getPriceForAdult(interval, room, treatment)
  }

  def map(room: RoomType)(treatment: Treatment)(day: MonthDay): Float = {

    (day, room, treatment) match {
      case (d, r, t) if E.contains(d) && r == RoomType.mountainView && t == Treatment.bb => 46.0F
      case (d, r, t) if E.contains(d) && r == RoomType.mountainView && t == Treatment.hb => 64.0F
      case (d, r, t) if E.contains(d) && r == RoomType.mountainView && t == Treatment.fb => 76.0F
      case (d, r, t) if E.contains(d) && r == RoomType.lakeView && t == Treatment.bb => 49.0F
      case (d, r, t) if E.contains(d) && r == RoomType.lakeView && t == Treatment.hb => 67.0F
      case (d, r, t) if E.contains(d) && r == RoomType.lakeView && t == Treatment.fb => 79.0F
      case (d, r, t) if E.contains(d) && r == RoomType.suite && t == Treatment.bb => 60.0F
      case (d, r, t) if E.contains(d) && r == RoomType.suite && t == Treatment.hb => 77.0F
      case (d, r, t) if E.contains(d) && r == RoomType.suite && t == Treatment.fb => 89.0F
      case (d, r, t) if E.contains(d) && r == RoomType.single && t == Treatment.bb => 52.0F
      case (d, r, t) if E.contains(d) && r == RoomType.single && t == Treatment.hb => 70.0F
      case (d, r, t) if E.contains(d) && r == RoomType.single && t == Treatment.fb => 82.0F
      //
      case (d, r, t) if D.contains(d) && r == RoomType.mountainView && t == Treatment.bb => 43.0F
      case (d, r, t) if D.contains(d) && r == RoomType.mountainView && t == Treatment.hb => 61.0F
      case (d, r, t) if D.contains(d) && r == RoomType.mountainView && t == Treatment.fb => 72.0F
      case (d, r, t) if D.contains(d) && r == RoomType.lakeView && t == Treatment.bb => 45.0F
      case (d, r, t) if D.contains(d) && r == RoomType.lakeView && t == Treatment.hb => 62.0F
      case (d, r, t) if D.contains(d) && r == RoomType.lakeView && t == Treatment.fb => 74.0F
      case (d, r, t) if D.contains(d) && r == RoomType.suite && t == Treatment.bb => 55.0F
      case (d, r, t) if D.contains(d) && r == RoomType.suite && t == Treatment.hb => 72.0F
      case (d, r, t) if D.contains(d) && r == RoomType.suite && t == Treatment.fb => 84.0F
      case (d, r, t) if D.contains(d) && r == RoomType.single && t == Treatment.bb => 50.0F
      case (d, r, t) if D.contains(d) && r == RoomType.single && t == Treatment.hb => 68.0F
      case (d, r, t) if D.contains(d) && r == RoomType.single && t == Treatment.fb => 80.0F
      //
      case (d, r, t) if C.contains(d) && r == RoomType.mountainView && t == Treatment.bb => 41.0F
      case (d, r, t) if C.contains(d) && r == RoomType.mountainView && t == Treatment.hb => 59.0F
      case (d, r, t) if C.contains(d) && r == RoomType.mountainView && t == Treatment.fb => 70.0F
      case (d, r, t) if C.contains(d) && r == RoomType.lakeView && t == Treatment.bb => 43.0F
      case (d, r, t) if C.contains(d) && r == RoomType.lakeView && t == Treatment.hb => 60.0F
      case (d, r, t) if C.contains(d) && r == RoomType.lakeView && t == Treatment.fb => 72.0F
      case (d, r, t) if C.contains(d) && r == RoomType.suite && t == Treatment.bb => 50.0F
      case (d, r, t) if C.contains(d) && r == RoomType.suite && t == Treatment.hb => 67.0F
      case (d, r, t) if C.contains(d) && r == RoomType.suite && t == Treatment.fb => 79.0F
      case (d, r, t) if C.contains(d) && r == RoomType.single && t == Treatment.bb => 48.0F
      case (d, r, t) if C.contains(d) && r == RoomType.single && t == Treatment.hb => 66.0F
      case (d, r, t) if C.contains(d) && r == RoomType.single && t == Treatment.fb => 78.0F
      //
      case (d, r, t) if B.contains(d) && r == RoomType.mountainView && t == Treatment.bb => 38.0F
      case (d, r, t) if B.contains(d) && r == RoomType.mountainView && t == Treatment.hb => 55.0F
      case (d, r, t) if B.contains(d) && r == RoomType.mountainView && t == Treatment.fb => 67.0F
      case (d, r, t) if B.contains(d) && r == RoomType.lakeView && t == Treatment.bb => 39.0F
      case (d, r, t) if B.contains(d) && r == RoomType.lakeView && t == Treatment.hb => 56.0F
      case (d, r, t) if B.contains(d) && r == RoomType.lakeView && t == Treatment.fb => 68.0F
      case (d, r, t) if B.contains(d) && r == RoomType.suite && t == Treatment.bb => 45.0F
      case (d, r, t) if B.contains(d) && r == RoomType.suite && t == Treatment.hb => 62.0F
      case (d, r, t) if B.contains(d) && r == RoomType.suite && t == Treatment.fb => 74.0F
      case (d, r, t) if B.contains(d) && r == RoomType.single && t == Treatment.bb => 43.0F
      case (d, r, t) if B.contains(d) && r == RoomType.single && t == Treatment.hb => 61.0F
      case (d, r, t) if B.contains(d) && r == RoomType.single && t == Treatment.fb => 73.0F
      //
      case (d, r, t) if A.contains(d) && r == RoomType.mountainView && t == Treatment.bb => 32.0F
      case (d, r, t) if A.contains(d) && r == RoomType.mountainView && t == Treatment.hb => 49.0F
      case (d, r, t) if A.contains(d) && r == RoomType.mountainView && t == Treatment.fb => 61.0F
      case (d, r, t) if A.contains(d) && r == RoomType.lakeView && t == Treatment.bb => 33.0F
      case (d, r, t) if A.contains(d) && r == RoomType.lakeView && t == Treatment.hb => 50.0F
      case (d, r, t) if A.contains(d) && r == RoomType.lakeView && t == Treatment.fb => 62.0F
      case (d, r, t) if A.contains(d) && r == RoomType.suite && t == Treatment.bb => 40.0F
      case (d, r, t) if A.contains(d) && r == RoomType.suite && t == Treatment.hb => 57.0F
      case (d, r, t) if A.contains(d) && r == RoomType.suite && t == Treatment.fb => 69.0F
      case (d, r, t) if A.contains(d) && r == RoomType.single && t == Treatment.bb => 39.0F
      case (d, r, t) if A.contains(d) && r == RoomType.single && t == Treatment.hb => 57.0F
      case (d, r, t) if A.contains(d) && r == RoomType.single && t == Treatment.fb => 69.0F
    }

  }

  case class Period(val intervals: (MonthDay, MonthDay)*) {
    def contains(day: MonthDay): Boolean = {
      intervals.exists(a => (a._1.isBefore(day) || a._1.isEqual(day)) && (a._2.isAfter(day) || a._2.isEqual(day)))
    }

    override def equals(that: Any): Boolean = {
      that match {
        case obj: Period => true
        case _ => false
      }
    }
  }

}