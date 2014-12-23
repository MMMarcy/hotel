package entities

import play.api.Play
import play.api.Play.current
import play.api.i18n._

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


  import entities.Mailer.mail._

  def raiseException() = throw new IllegalArgumentException()

  def notifyPrenotation(prenotation: Booking)(implicit lang: Lang) = {
    send a new Mail(
      from = (mailHotel, "Hotel Ancora Staff"),
      to = prenotation.email,

      subject = Messages("mail.object"),
      message = createBodyForEmail(prenotation)
    )
    if (Play.isProd) {
      send a new Mail(
        from = (mailHotel, "Hotel Ancora Site"),
        to = mailHotel,
        subject = "Nuova prenotazione - " + prenotation.firstName + " " + prenotation.lastName,
        message = prenotation.summary
      )
    }
  }


  private def createBodyForEmail(prenotation: Booking)(implicit lang: Lang): String = {
    val text = views.html.common.mailTemplate.render(prenotation,lang)
    text.toString()
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