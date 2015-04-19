package controllers

import play.api.Play.current
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.mailer.{Email, MailerPlugin}
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

    val confirmationEmail = Email(
      "Confirmation Of Your Enquiry In Arty Monkeys",
      "No Reply Arty Monkeys <" + NO_REPLY_ARTY_MONKEYS + ">",
      Seq(name + " <" + email + ">"),

      bodyText = Some("Thanks your enquiry to Arty Monkeys.\n\n\n" +
        "We'll respond to your query as soon as possible.\n\n\n" +
        "Kind Regards,\n\nYvonne & Kelley\n"
        + "\n\n*** This is an automated message, please don't reply directly. Thanks! ***")
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
