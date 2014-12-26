package models

import RoomType._
import play.api.i18n._

object UtilObject {

  val ancoraCoord = ("Hotel Ancora","hotel", 45.574867F, 10.70715F)
  val parkCoord = ("Parking","park", 45.57634368F, 10.71388274F)

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
  		case "lakeview" => RoomType.lakeView
  		case "mountainview" => RoomType.mountainView
  		case "suite" => RoomType.suite
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