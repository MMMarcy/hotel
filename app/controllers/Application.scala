package controllers

import play.api._
import play.api.mvc._
import models._
import play.api.Logger
import entities._
import org.joda.time.MonthDay
import play.api.data._
import play.api.data.Forms._
import play.api.i18n._
import play.api.Play.current

object Application extends Controller {

  val bookingForm = Form {
    mapping(
      Booking.fromSTR -> nonEmptyText,
      Booking.toSTR -> nonEmptyText,
      Booking.adultsSTR -> number(min = 1),
      Booking.bigKidsSTR -> number(min = 0),
      Booking.smallKidsSTR -> number(min = 0),
      Booking.infantsSTR -> number(min = 0),
      Booking.treatmentSTR -> nonEmptyText,
      Booking.roomSTR -> nonEmptyText,
      Booking.emailSTR -> email,
      Booking.phoneSTR -> nonEmptyText,
      Booking.firstNameSTR -> nonEmptyText,
      Booking.lastNameSTR -> nonEmptyText,
      Booking.nonRefundableSTR -> boolean,
      Booking.notesSTR -> optional(text))(Booking.apply)(Booking.unapply)
  }

  def index = Action { implicit request =>
    Ok(views.html.pages.index())
  }

  def accomodation = Action { implicit request =>
    Ok(views.html.pages.accomodation())
  }

  def services = Action { implicit request =>
    Ok(views.html.pages.services())
  }

  def showRoom(roomType: String) = Action { implicit request =>
    Ok(views.html.pages.room(UtilObject.stringToRoomType(roomType.toLowerCase)))
  }

  def aroundUs = Action { implicit request =>
    Ok(views.html.pages.aroundus())
  }

  def contacts = Action { implicit request =>
    Ok(views.html.pages.contacts())
  }

  def sendEmail = Action { implicit request =>
    bookingForm.bindFromRequest.fold(
      formWithErrors => {
        // binding failure, you retrieve the form containing errors:
        BadRequest(views.html.pages.prenotationForm(formWithErrors))
      },
      booking => {
        Mailer.notifyPrenotation(booking)
        Ok(views.html.pages.bookingSuccessful())
      })
  }

  def showForm = Action { implicit request =>
    Ok(views.html.pages.prenotationForm(bookingForm))
  }

  def language(code: String) = Action { implicit request =>
    Redirect(request.headers.get(REFERER).getOrElse("/"))
      .withLang(Lang(code))
  }

}