package models

import org.joda.time.MonthDay
import play.api.Play.current
import play.api.cache.Cache
import play.api.i18n.{ Lang, Messages }

object RoomType extends Enumeration {
  type RoomType = Value
  val single, double, triple, quad = Value
}

object Treatment extends Enumeration {
  type Treatment = Value
  val bb, hb, fb = Value
}

object RoomView extends Enumeration {
  type RoomView = Value
  val lakeView, mountainView = Value
}

object PriceObject {

  import models.RoomType._
  import models.Treatment._
  import models.RoomView._

  val A = Period((new MonthDay(1, 7), new MonthDay(3, 28)), (new MonthDay(10, 6), new MonthDay(12, 15)))
  val B = Period((new MonthDay(4, 12), new MonthDay(5, 23)))
  val C = Period((new MonthDay(3, 29), new MonthDay(4, 11)), (new MonthDay(12, 6), new MonthDay(12, 21)))
  val D = Period((new MonthDay(5, 24), new MonthDay(7, 5)), (new MonthDay(9, 20), new MonthDay(10, 5)))
  val E = Period((new MonthDay(7, 6), new MonthDay(9, 19)), (new MonthDay(12, 22), new MonthDay(12, 31)), (new MonthDay(1, 1), new MonthDay(1, 6)))

  protected val PRICESTRING = "formattedPrices"
  protected val roomTypes = List(lakeView, mountainView, suite, single)
  protected val treatments = List(fb, hb, bb)
  protected val periods = List[Period](A, B, C, D, E)
  protected val periodsLabels = List[Char]('A', 'B', 'C', 'D', 'E')

  def printTable(roomType: RoomType)(implicit lang: Lang): String = {
    val stringBuilder = new StringBuilder()
    stringBuilder.append("<thead>")
    stringBuilder.append("<tr><th></th><th>A</th><th>B</th><th>C</th><th>D</th><th>E</th></tr>")
    stringBuilder.append("</thead>")
    stringBuilder.append("<tbody>")
    for (treatment <- treatments) {
      stringBuilder.append("<tr>")
      stringBuilder.append("<td>" + Messages("treatment." + treatment.toString) + "</td>")
      for (period <- periods) {
        stringBuilder.append("<td class=\"moneyValue\">" + map(roomType)(treatment)(period.intervals.head._1) + "&euro;</td>")
      }
      stringBuilder.append("</tr>")
    }
    stringBuilder.append("</tbody>")
    stringBuilder.toString()
  }

  def printPeriods = {
    val stringBuilder = new StringBuilder()
    periods.zip(periodsLabels).foreach {
      case (period, label) =>
        stringBuilder.append("""<tr class="period-holder" > """)
        stringBuilder.append("<td>")
        stringBuilder.append(label)
        stringBuilder.append("</td>")

        stringBuilder.append("<td>")
        period.intervals.foreach { interval =>
          stringBuilder.append(interval._1.toString("dd/MM"))
          stringBuilder.append(" - ")
          stringBuilder.append(interval._2.toString("dd/MM"))
          stringBuilder.append("<br/>")
        }
        stringBuilder.append("</td>")
        stringBuilder.append("</tr>")
    }
    stringBuilder.toString()
  }

  def printRow(room: RoomType, treatment: Treatment): String = {
    Cache.getAs[String](PRICESTRING + room.toString + treatment.toString) match {
      case Some(str) => str
      case None => {
        val strbld = new StringBuilder()
        val partialFun = map(room)(treatment)(_)
        periods.foreach(period => {
          val day = period.intervals.head._1
          strbld.append("""<div class="col-xs-1 val text-center">""")
          strbld.append(UtilObject.format(partialFun(day)))
          strbld.append("</div>")
        })
        val res = strbld.toString()
        Cache.set(PRICESTRING + room.toString + treatment.toString, res, 43200)
        res
      }
    }

  }

  /*  def map(room: RoomType)(treatment: Treatment)(day: MonthDay): Float = {

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

    }*/

  def getPriceForChildren(age: Int, interval: (MonthDay, MonthDay), room: RoomType, treatment: Treatment): List[Float] = {
    if (age <= 2) getPriceForAdult(interval, room, treatment).map(_ * 0F)
    else if (age <= 6) getPriceForAdult(interval, room, treatment).map(_ * 0.5F)
    else if (age <= 11) getPriceForAdult(interval, room, treatment).map(_ * 0.8F)
    else getPriceForAdult(interval, room, treatment)
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

  def map(roomType: RoomType)(roomView: RoomView)(treatment: Treatment)(day: MonthDay): Int = {

    (roomType, roomView, treatment, day) match {

        //DOUBLE ROOM

      case (RoomType.double, RoomView.mountainView, Treatment.bb, d) =>
        if (A contains d) 64
        else if (B contains d) 76
        else if (C contains d) 82
        else if (D contains d) 86
        else 92

      case (RoomType.double, RoomView.mountainView, Treatment.hb, d) =>
        if (A contains d) 98
        else if (B contains d) 110
        else if (C contains d) 118
        else if (D contains d) 122
        else 128

      case (RoomType.double, RoomView.mountainView, Treatment.fb, d) =>
        if (A contains d) 122
        else if (B contains d) 134
        else if (C contains d) 140
        else if (D contains d) 144
        else 152

      case (RoomType.double, RoomView.lakeView, Treatment.bb, d) =>
        if (A contains d) 68
        else if (B contains d) 80
        else if (C contains d) 86
        else if (D contains d) 90
        else 98

      case (RoomType.double, RoomView.lakeView, Treatment.hb, d) =>
        if (A contains d) 102
        else if (B contains d) 114
        else if (C contains d) 120
        else if (D contains d) 124
        else 134

      case (RoomType.double, RoomView.lakeView, Treatment.fb, d) =>
        if (A contains d) 124
        else if (B contains d) 136
        else if (C contains d) 144
        else if (D contains d) 148
        else 158


        //TRIPLE ROOM

      case (RoomType.triple, _, Treatment.bb, d) =>
        if (A contains d) 120
        else if (B contains d) 135
        else if (C contains d) 150
        else if (D contains d) 165
        else 180

      case (RoomType.triple, _, Treatment.hb, d) =>
        if (A contains d) 171
        else if (B contains d) 186
        else if (C contains d) 201
        else if (D contains d) 216
        else 231

      case (RoomType.triple, _, Treatment.fb, d) =>
        if (A contains d) 207
        else if (B contains d) 222
        else if (C contains d) 236
        else if (D contains d) 252
        else 267


      //QUAD ROOM
      case (RoomType.quad, _, Treatment.bb, d) =>
        if (A contains d) 136
        else if (B contains d) 160
        else if (C contains d) 172
        else if (D contains d) 180
        else 196

      case (RoomType.quad, _, Treatment.hb, d) =>
        if (A contains d) 204
        else if (B contains d) 228
        else if (C contains d) 240
        else if (D contains d) 248
        else 268

      case (RoomType.quad, _, Treatment.fb, d) =>
        if (A contains d) 248
        else if (B contains d) 272
        else if (C contains d) 288
        else if (D contains d) 296
        else 316

        //SINGLE

      case (RoomType.single, _, Treatment.bb, d) =>
        if (A contains d) 39
        else if (B contains d) 43
        else if (C contains d) 48
        else if (D contains d) 50
        else 52

      case (RoomType.single, _, Treatment.hb, d) =>
        if (A contains d) 57
        else if (B contains d) 61
        else if (C contains d) 66
        else if (D contains d) 68
        else 70

      case (RoomType.single, _, Treatment.fb, d) =>
        if (A contains d) 69
        else if (B contains d) 73
        else if (C contains d) 78
        else if (D contains d) 80
        else 82


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