package entities

import play.api.i18n._
import scala.util.Properties
import models.UtilObject

case class Booking(from: String, to: String, adults: Int, bigKids: Int,
  smallKids: Int, infants: Int, treatment: String, room: String,
  email: String, phone: String, firstName: String, lastName: String, 
  nonRefundable: Boolean, notes: Option[String]) {

  def summary(implicit lang: Lang): String = {
    val str = new StringBuilder()
    //FirstName
    str.append(Messages("form.first-name"))
    str.append(": " + firstName)
    str.append(Booking.newLine)

    //LastName
    str.append(Messages("form.last-name"))
    str.append(": " + lastName)
    str.append(Booking.newLine)

    //Email
    str.append(Messages("form.email"))
    str.append(": " + email)
    str.append(Booking.newLine)

    //Phone
    str.append(Messages("form.phone"))
    str.append(": " + phone)
    str.append(Booking.newLine)

    //Arrival date
    str.append(Messages("form.arrival-date"))
    str.append(": " + from)
    str.append(Booking.newLine)

    //Departure date
    str.append(Messages("form.departure-date"))
    str.append(": " + to)
    str.append(Booking.newLine)

    //Adults
    str.append(Messages("form.adults"))
    str.append(": " + adults)
    str.append(Booking.newLine)

    //Big kids
    str.append(Messages("form.big-kids"))
    str.append(": " + bigKids)
    str.append(Booking.newLine)

    //Small kids
    str.append(Messages("form.small-kids"))
    str.append(": " + smallKids)
    str.append(Booking.newLine)

    //Infants
    str.append(Messages("form.infants"))
    str.append(": " + infants)
    str.append(Booking.newLine)

    //RoomType
    str.append(Messages("form.room-type"))
    str.append(": " + UtilObject.localizeRoomType(room))
    str.append(Booking.newLine)

    //Treatment type
    str.append(Messages("form.treatment-type"))
    str.append(": " + UtilObject.localizeTreatment(treatment))
    str.append(Booking.newLine)

    //Discount
    str.append(Messages("form.use-discount"))
    if (nonRefundable)
      str.append(": " + Messages("form.true"))
    else
      str.append(": " + Messages("form.false"))
    str.append(Booking.newLine)

    //Notes
    str.append(Messages("form.notes"))
    str.append(": " + notes.getOrElse{Messages("form.no-notes")})
    str.append(Booking.newLine)
    
    str.toString


  }

}

object Booking {
  val fromSTR = "from"
  val toSTR = "to"
  val adultsSTR = "adults"
  val bigKidsSTR = "bigKids"
  val smallKidsSTR = "smallKids"
  val infantsSTR = "infants"
  val treatmentSTR = "treatment"
  val roomSTR = "room"
  val emailSTR = "email"
  val phoneSTR = "phone"
  val firstNameSTR = "firstName"
  val lastNameSTR = "lastName"
  val nonRefundableSTR = "nonRefundable"
  val notesSTR = "notes"

  //Util
  private val newLine = Properties.lineSeparator
}