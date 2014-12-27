package models

import controllers.routes

/**
 * Created by marcello on 27/12/14.
 */
object LanguageHandlerObject {

  private val languages = List(
    "it" -> "Italiano",
    "en" -> "English",
    "de" -> "Deutsch",
    "fr" -> "Francoise",
    "es" -> "Español",
    "sv" -> "Svenska"
  )

  def printLanguages: String = {
    val builder = new StringBuilder();
    languages.foreach(s => {
      val (code, string) = s
      builder.append("<li><a href='")
      builder.append(routes.Application.language(code).url)
      builder.append("'>")
      builder.append(string)
      builder.append("</a></li>")
    })
    builder.toString()
  }

}
