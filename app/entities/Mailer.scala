package entities

import org.apache.commons.mail._
import models._
import models.RoomType._
import models.Treatment._
import org.joda.time.MonthDay
import play.api.{Play, Logger}
import play.api.i18n._
import play.api.Play.current
import scala.language.implicitConversions
import scala.util.Properties


object Mailer {

  val mailConf = Play.current.configuration.getConfig("mail")

  val hostName = mailConf.map(conf =>
    conf getString "connection.address" getOrElse raiseException())
    .get

  val port = mailConf.map(conf =>
    conf getInt "connection.port" getOrElse raiseException())
    .get

  val sslConnection = mailConf.map(conf =>
    conf getBoolean "connection.useSSL" getOrElse raiseException())
    .get

  val username = mailConf.map(conf =>
    conf getString "hotel.username" getOrElse raiseException())
    .get

  val password = mailConf.map(conf =>
    conf getString "hotel.password" getOrElse raiseException())
    .get

  val mailHotel = mailConf.map(conf =>
    conf getString "hotel.email" getOrElse raiseException())
    .get

  val newLine = Properties.lineSeparator


  import mail._

  def raiseException() = throw new IllegalArgumentException()

  def notifyPrenotation(prenotation: Prenotation)(implicit lang: Lang) = {
    send a new Mail(
      from = (mailHotel, "Hotel Ancora Staff"),
      to = prenotation.email,

      subject = Messages("mail.object"),
      message = createBodyForEmail(prenotation)
    )

    /*send a new Mail(
      from = (mailHotel, "Hotel Ancora Site"),
      to = mailHotel,
      subject = "Nuova prenotazione - " + prenotation.firstName + " " + prenotation.lastName,
      message = prenotation.summary
    )*/
  }

  //TODO: create an HTML
  private def createBodyForEmail(prenotation: Prenotation)(implicit lang: Lang): String = {

    val str = new StringBuilder()

    def appendNLines[A, B](n: Int): Unit =
      if (n > 0) {
        str.append(newLine)
        appendNLines(n - 1)
      }

    str.append(Messages("mail.heading", prenotation.lastName))
    appendNLines(2)
    str.append(Messages("mail.intro"))
    appendNLines(2)
    str.append(Messages("mail.summary-of-prenotation"))
    appendNLines(1)
    str.append(prenotation.summary)
    appendNLines(2)
    str.append(Messages("mail.greetings"))
    appendNLines(3)
    str.append(Messages("Hotel Ancora staff"))
    appendNLines(3)
    str.toString
  }

  object mail {

    implicit def stringToSeq(single: String): Seq[String] = Seq(single)

    implicit def liftToOption[T](t: T): Option[T] = Some(t)

    sealed abstract class MailType

    case class Mail(
                     from: (String, String), // (email -> name)
                     to: Seq[String],
                     cc: Seq[String] = Seq.empty,
                     bcc: Seq[String] = Seq.empty,
                     subject: String,
                     message: String,
                     richMessage: Option[String] = None,
                     attachment: Option[(java.io.File)] = None
                     )

    case object Plain extends MailType

    case object Rich extends MailType

    case object MultiPart extends MailType

    object send {
      def a(mail: Mail) {
        import org.apache.commons.mail._

        val format =
          if (mail.attachment.isDefined) MultiPart
          else if (mail.richMessage.isDefined) Rich
          else Plain

        val commonsMail: Email = format match {
          case Plain => new SimpleEmail().setMsg(mail.message)
          case Rich => new HtmlEmail().setHtmlMsg(mail.richMessage.get).setTextMsg(mail.message)
          case MultiPart => {
            val attachment = new EmailAttachment()
            attachment.setPath(mail.attachment.get.getAbsolutePath)
            attachment.setDisposition(EmailAttachment.ATTACHMENT)
            attachment.setName(mail.attachment.get.getName)
            new MultiPartEmail().attach(attachment).setMsg(mail.message)
          }
        }

        mail.to foreach commonsMail.addTo
        mail.cc foreach commonsMail.addCc
        mail.bcc foreach commonsMail.addBcc

        commonsMail.setHostName(hostName)
        commonsMail.setSmtpPort(port)
        commonsMail.setAuthenticator(new DefaultAuthenticator(username, password))
        commonsMail.setSSLOnConnect(sslConnection)

        commonsMail
          .setFrom(mail.from._1, mail.from._2)
          .setSubject(mail.subject)
          .send()
      }
    }

  }

}