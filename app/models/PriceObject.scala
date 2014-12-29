package models

import org.joda.time.MonthDay
import play.api.Play.current
import play.api.cache.Cache
import play.api.i18n.{Lang, Messages}

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
  protected val roomTypes = List(lakeView, mountainView)
  protected val treatments = List(fb, hb, bb)
  protected val periods = List[Period](A, B, C, D, E)
  protected val periodsLabels = List[Char]('A', 'B', 'C', 'D', 'E')

  def printTable(roomType: RoomType)(views: RoomView*)(implicit lang: Lang): String = {
    val stringBuilder = new StringBuilder()
    stringBuilder.append("<table class=\"price-holder\">")
    stringBuilder.append("<thead>")
    stringBuilder.append("<tr><th></th><th>A</th><th>B</th><th>C</th><th>D</th><th>E</th></tr>")
    stringBuilder.append("</thead>")
    stringBuilder.append("<tbody>")
    for (view <- views) {
      stringBuilder.append("<tr>")
      stringBuilder.append("<td>")
      //TODO: externalize the string
      stringBuilder.append(Messages("room."+view.toString))
      stringBuilder.append("</td>")
      for (period <- periods) {
        stringBuilder.append("<td>")
        for (treatment <- treatments) {
          stringBuilder.append("<span class=\"moneyValue\">" + map(roomType)(view)(treatment)(period.intervals.head._1) + "&euro;</span>")
          stringBuilder.append("<br>")
        }
        stringBuilder.append("</td>")
      }
      stringBuilder.append("</tr>")
    }
    stringBuilder.append("</tbody>")
    stringBuilder.append("</table>")
    stringBuilder.toString()
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

  def printPeriods = {
    val stringBuilder = new StringBuilder()
    periods.zip(periodsLabels).foreach {
      case (period, label) =>
        stringBuilder.append( """<tr class="period-holder" > """)
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



  case class Period(intervals: (MonthDay, MonthDay)*) {
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