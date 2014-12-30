package models

import play.api.i18n.Lang

/**
 * Created by marcello on 30/12/14.
 */
object BookingLinkHandler {

  def getLinkWithLang(implicit lang: Lang): String = {
    lang.language match {
      case "it" => "https://secure.kosmosol.it/booking/check-availability.php?h=ancora-garda&l=it"
      case "es" => "https://secure.kosmosol.it/booking/check-availability.php?h=ancora-garda&l=es"
      case "de" => "https://secure.kosmosol.it/booking/check-availability.php?h=ancora-garda&l=de"
      case "fr" => "https://secure.kosmosol.it/booking/check-availability.php?h=ancora-garda&l=fr"
      case _    => "https://secure.kosmosol.it/booking/check-availability.php?h=ancora-garda&l=en"
    }
  }

}
