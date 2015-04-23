package controllers

import java.io.File

import org.apache.commons.mail.EmailAttachment
import play.api.Play.current
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.mailer.{AttachmentData, AttachmentFile, Email, MailerPlugin}
import play.api.mvc.{Action, Results}

trait EmailInterestRegistered extends EmailSenderBase {
  this: Results =>

  val interestForm = Form(
    tuple(
      "name" -> text,
      "phone" -> text,
      "email" -> text
    )
  )

  def emailOfRegisteredInterest = Action { implicit request =>
    var error = false
    val (name, phone, email) = interestForm.bindFromRequest.get

    val emailOfRegistrationInterestToInfoArtyMonkeys = Email(
      "Registration Of Interest",
      "No Reply Arty Monkeys <" + NO_REPLY_ARTY_MONKEYS + ">",
      Seq("Info Arty Monkeys <" + INFO_ARTY_MONKEYS + ">"),

      bodyText = Some("Name : " + name + "\n" +
        "Phone: " + phone + "\n" +
        "Email: " + email + "\n")

    )

    try {
      MailerPlugin.send(emailOfRegistrationInterestToInfoArtyMonkeys)
    } catch {
      case ex: Exception =>
        println("oh oh : " + ex)
        error = true
    }

    val thankYou1OpeningLine = "Thanks for your interest in Arty Monkeys."
    val thankYou2Body = "We'll be in touch soon."
    val thankYou3KindRegards = "Kind Regards,"
    val thankYou4ArtyMonkeys = "Yvonne & Kelley"
    val thankYou5AutoMessage = "*** This is an automated message, please don't reply directly. Thanks! ***"

    val confirmationEmail = Email(
      "Confirmation Of Your Interest In Arty Monkeys",
      "No Reply Arty Monkeys <" + NO_REPLY_ARTY_MONKEYS + ">",
      Seq(name + " <" + email + ">"),

      attachments = Seq(
        AttachmentFile("artyMonkeysLogoCrop.jpg", new File(current.classloader.getResource("public/images/artyMonkeysLogoCrop.jpg").getPath))
      ),

      bodyText = Some(
        thankYou1OpeningLine + "\n\n" +
          thankYou2Body + "\n\n\n" +
          thankYou3KindRegards + "\n\n" +
          thankYou4ArtyMonkeys + "\n\n\n\n" +
          thankYou5AutoMessage + "\n\n\n"
      ),

      bodyHtml = Some( """
      <html>
      <head>
<style>
body {background-color: lightgray}
p {
  color: purple
  font-weight: bold;
}
</style>
</head>
        <body bgcolor=”#ffffff”>
                       """ +
        "<p>" + thankYou1OpeningLine + "</p>" +
        "<p>" + thankYou2Body + "</p><br>" +
        "<p>" + thankYou3KindRegards + "</p>" +
        "<p>" + thankYou4ArtyMonkeys + "</p><br><br>" +
        "<p>" + thankYou5AutoMessage + "</p>" +
        "</body></html>")
    )

    try {
      MailerPlugin.send(confirmationEmail)
    } catch {
      case ex: Exception =>
        println("oh2 oh2 : " + ex)
        error = true
    }

    if (error) {
      Ok(views.html.errorsendingemail())
    } else {
      Ok(views.html.thankyou())
    }
  }
}
