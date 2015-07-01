package controllers.email

import controllers.ApplicationCake
import model._
import play.api.Play.current
import play.api.libs.mailer._
import play.api.mvc._

trait EmailEnquiry extends EmailSenderBase
{
  this: Results =>

  def emailOfEnquiry = Action { implicit request =>
    val customer = buildCustomerFromRequest(request)
    ApplicationCake.customerService.save(customer)
    handleEmailSending(customer)
  }

  override def sendEmails(customer: Customer): Boolean =
  {
    var error = false

    val littleMonkeyDetails = for (monkey <- customer.monkeys) yield {
      "\tName: " + monkey.name.getOrElse("Not Provided") +
        "   Dob: " + monkey.dob.getOrElse("Not Provided") + "\n"
    }

    val emailEnquiry = Email(
      "Enquiry To Arty Monkeys",
      "No Reply Arty Monkeys <" + NO_REPLY_ARTY_MONKEYS + ">",
      Seq("Info Arty Monkeys <" + INFO_ARTY_MONKEYS + ">"),

      bodyText = Some(
        "Customer Name: " + customer.knownas.getOrElse("Not Provided") + "\n" +
          "Email: " + customer.emailWrapper.email.getOrElse("Not Provided") + "\n" +
          "Phone: " + customer.phoneWrapper.phone.getOrElse("Not Provided") + "\n" +
          "Little Monkeys :\n" + littleMonkeyDetails.mkString(" ") +
          "\n\nPreferred Location: " + customer.customerPreferences.location.getOrElse("Not Provided") + "\n" +
          "Preferred Time: " + customer.customerPreferences.time.getOrElse("Not Provided") + "\n" +
          "\n\nHow Did They Hear About Arty Monkeys: " + customer.howDidYouHear.category.getOrElse("Not Provided") + "\n" +
          "Any Extra Details How They Heard: " + customer.howDidYouHear.extraText.getOrElse("Not Provided") + "\n" +
          "\nCustomer Message: " + customer.message.getOrElse("Not Provided") + "\n\n\n"
      )
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
      Seq(customer.knownas.get + " <" + customer.emailWrapper.email.get + ">"),

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

    error
  }
}
