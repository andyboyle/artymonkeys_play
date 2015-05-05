package controllers

import play.Play
import play.api.Play.current
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.mailer._
import play.api.mvc.{Action, Results}

trait EmailEnquiry extends EmailSenderBase {
  this: Results =>

  val enquiryForm = Form(
    tuple(
      "name" -> text,
      "phone" -> text,
      "email" -> text,
      "message" -> text
    )
  )

  def emailOfEnquiry = Action { implicit request =>
    var error = false
    val (name, phone, email, message) = enquiryForm.bindFromRequest.get

    val emailEnquiry = Email(
      "Enquiry To Arty Monkeys",
      "No Reply Arty Monkeys <" + NO_REPLY_ARTY_MONKEYS + ">",
      Seq("Info Arty Monkeys <" + INFO_ARTY_MONKEYS + ">"),

      bodyText = Some("Name : " + name + "\n" +
        "Phone: " + phone + "\n" +
        "Email: " + email + "\n" +
        "\nMessage:\n\n" + message)

    )

    try {
      MailerPlugin.send(emailEnquiry)
    } catch {
      case ex: Exception =>
        println("oh oh : " + ex)
        error = true
    }

    val thankYouEnquiryLine1 = "Thanks for your enquiry to Arty Monkeys."
    val thankYouEnquiryLine2 = "We'll respond to your query as soon as possible."
    val thankYouEnquiryLine3 = "Kind Regards,"
    val thankYouEnquiryLine4 = "Yvonne & Kelley"
    val thankYouEnquiryLine5 = "*** This is an automated message, please don't reply directly. Thanks! ***"

    val confirmationEmail = Email(
      "Confirmation Of Your Enquiry In Arty Monkeys",
      "No Reply Arty Monkeys <" + NO_REPLY_ARTY_MONKEYS + ">",
      Seq(name + " <" + email + ">"),

      attachments = Seq(
        AttachmentFile("artyMonkeysLogoCrop.jpg", Play.application().getFile("conf/artyMonkeysLogoCrop.jpg"))
      ),

      bodyText = Some(thankYouEnquiryLine1 + "\n\n\n" +
        thankYouEnquiryLine2 + "\n\n\n" +
        thankYouEnquiryLine3 + "\n\n" + "thankYouEnquiryLine4\n"
        + "\n\n" + thankYouEnquiryLine5),

      bodyHtml = Some(
        """
      <html>
      <head>
<style>
body {
  background-color: lightgray;
}
p {
  color: purple;
  font-weight: bold;
}
</style>
</head>
        <body bgcolor=”#ffffff”>
        """ +
          "<p>" + thankYouEnquiryLine1 + "</p>" +
          "<p>" + thankYouEnquiryLine2 + "</p><br>" +
          "<p>" + thankYouEnquiryLine3 + "</p>" +
          "<p>" + thankYouEnquiryLine4 + "</p><br><br>" +
          "<p>" + thankYouEnquiryLine5 + "</p>" +
          "</body></html>"
      )
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
