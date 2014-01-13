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

  val prenotationForm = Form {
    mapping(
      Prenotation.fromSTR -> nonEmptyText,
      Prenotation.toSTR -> nonEmptyText,
      Prenotation.adultsSTR -> number(min = 1),
      Prenotation.bigKidsSTR -> number(min = 0),
      Prenotation.smallKidsSTR -> number(min = 0),
      Prenotation.infantsSTR -> number(min = 0),
      Prenotation.treatmentSTR -> nonEmptyText,
      Prenotation.roomSTR -> nonEmptyText,
      Prenotation.emailSTR -> email,
      Prenotation.phoneSTR -> nonEmptyText,
      Prenotation.firstNameSTR -> nonEmptyText,
      Prenotation.lastNameSTR -> nonEmptyText,
      Prenotation.nonRefundableSTR -> boolean,
      Prenotation.notesSTR -> optional(text))(Prenotation.apply)(Prenotation.unapply)
  }

  def index = Action { implicit request =>
    Ok(views.html.index())
  }

  def accomodation = Action { implicit request =>
    Ok(views.html.accomodation())
  }

  def services = Action { implicit request =>
    Ok(views.html.services())
  }

  def showRoom(roomType: String) = Action { implicit request =>
    Ok(views.html.room(UtilObject.stringToRoomType(roomType.toLowerCase)))
  }
  def aroundUs = Action { implicit request =>
    Ok(views.html.aroundus())
  }

  def contacts = Action { implicit request =>
    Ok(views.html.contacts())
  }

  def sendEmail = Action { implicit request =>
    prenotationForm.bindFromRequest.fold(
      formWithErrors => {
        // binding failure, you retrieve the form containing errors:
        BadRequest(views.html.prenotationForm(formWithErrors))
      },
      prenotation => {
        //TODO:successful page
        Mailer.notifyPrenotation(prenotation)
        Ok(views.html.bookingSuccessful())
      })
  }

  def showForm = Action { implicit request =>
    Ok(views.html.prenotationForm(prenotationForm))
  }

  def language(code: String) = Action { implicit request =>
    Redirect(request.headers.get(REFERER).getOrElse("/"))
      .withLang(Lang(code))
  }

}