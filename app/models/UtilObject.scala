package models

import RoomType._
import play.api.Play
import play.api.i18n._

object UtilObject {

  val ancoraCoord = ("Hotel Ancora","hotel", 45.574867F, 10.70715F)
  val parkCoord = ("Parking","park", 45.57634368F, 10.71388274F)

  val discountMinDays = Play.current.configuration.getInt("discount.days").get
  val discountPercentage = Play.current.configuration.getInt("discount.percentage").get
  val discountForMeals = Play.current.configuration.getInt("discount.mealsForGuests").get


  def createStringForDatePickerLocalization(implicit lang: Lang): String = {
    val builder = new StringBuilder("ui/i18n/datepicker-")
    val countryString = lang.language match {
      case "en" => "en-GB"
      case _    => lang.language
    }
    builder.append(countryString)
    builder.append(".js")
    builder.toString()
  }

  def format(float: Float): String = {
    val strBuild = new StringBuilder(f"$float%-9.2f ")
    strBuild.append("€")
    strBuild.toString()
  }

  def printGeoCoord(entity: (String, String, Float, Float)) : String = {
    val strBuild = new StringBuilder("geo:0,0?q=")
    strBuild.append(entity._3)
    strBuild.append(",")
    strBuild.append(entity._4)
    strBuild.append("(Hotel Ancora)")
    strBuild.toString()
  }

  def stringToRoomType(roomType: String): RoomType = {
  	roomType match {
  		case "lakeview" => RoomType.single
  		case "mountainview" => RoomType.double
  		case "suite" => RoomType.triple
  		case "single" => RoomType.single
  		case _ => throw new Exception()
  	}
  }

  def localizeRoomType(roomType: String)(implicit lang: Lang): String = {
    roomType match {
      case "lakeView" => Messages("room.lake-view")
      case "mountainView"=> Messages("room.mountain-view")
      case "suite" => Messages("room.suite")
      case "single" => Messages("room.single")
      case _ => throw new Exception()
    }    
  }

  def localizeTreatment(treatment: String)(implicit lang: Lang): String = {
    treatment match {
      case "bb" => Messages("treatment.bb") 
      case "hb" => Messages("treatment.hb")
      case "fb" => Messages("treatment.fb")
      case _ => throw new Exception()
    }
    
  }

}