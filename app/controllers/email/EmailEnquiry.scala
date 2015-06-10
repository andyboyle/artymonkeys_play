package controllers.email

import model._
import play.Play
import play.api.Play.current
import play.api.libs.mailer._
import play.api.mvc.{Action, Results}

trait EmailEnquiry extends EmailSenderBase {
  this: Results =>

  def emailOfEnquiry = Action { implicit request =>
    var error = false

    var customerName = ""
    var customerPhone = ""
    var customerEmail = ""
    var customerPreferredVenue = ""
    var customerPreferredClassTime = ""
    var customerNumberOfMonkeys = ""
    var customerMonkeyNames = scala.collection.mutable.ListBuffer.empty[String]
    var customerMonkeyDobs = scala.collection.mutable.ListBuffer.empty[String]
    var customerHowDidYouHear = ""
    var customerHowDidYouHearExtra = ""
    var customerMessage = ""

    val monkeyNamePattern = "monkey(\\d+)name".r
    val monkeyDobPattern = "monkey(\\d+)dob".r

    val formDetailsOption = request.body.asFormUrlEncoded
    if ( formDetailsOption.isDefined ) {
      val formDetails = formDetailsOption.get
      for ( key <- formDetails.keys ) {
        val formValue = formDetails(key)
        key match {
          case "name" => customerName = formValue.head
          case "email" => customerEmail = formValue.head
          case "phone" => customerPhone = formValue.head
          case "location" => customerPreferredVenue = formValue.head
          case "time" => customerPreferredClassTime = formValue.head
          case "numberofmonkeys" => customerNumberOfMonkeys = formValue.head
          case "howdidyouhear" => customerHowDidYouHear = formValue.head
          case "howdidyouhearextra" => customerHowDidYouHearExtra = formValue.head
          case "message" => customerMessage = formValue.head
          case monkeyNamePattern(key) => customerMonkeyNames += formValue.head
          case monkeyDobPattern(key) => customerMonkeyDobs += formValue.head
        }
      }
    }

    val monkeyTuple =  customerMonkeyNames zip customerMonkeyDobs
    val monkeysList = for ( monkey <- monkeyTuple ) yield {
      Monkey(Some(monkey._1), Some(monkey._2))
    }

    val customer = new Customer(
      Some(customerName),
      EmailWrapper(Some(customerEmail)),
      PhoneWrapper(Some(customerPhone)),
      new CustomerPreferences(Some(customerPreferredVenue), Some(customerPreferredClassTime)),
      monkeysList.toList,
      new HowDidYouHear(Some(customerHowDidYouHear), Some(customerHowDidYouHearExtra)),
      Some(customerMessage)
    )

    customerDao.addCustomer(customer)

    val emailEnquiry = Email(
      "Enquiry To Arty Monkeys",
      "No Reply Arty Monkeys <" + NO_REPLY_ARTY_MONKEYS + ">",
      Seq("Info Arty Monkeys <" + INFO_ARTY_MONKEYS + ">"),

      bodyText = Some(customer.toString)
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
