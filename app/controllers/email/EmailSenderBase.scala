package controllers.email

import model._
import org.joda.time.LocalDateTime
import play.api.Play
import play.api.mvc.Results.Ok
import play.api.mvc.{AnyContent, Request, Result}

trait EmailSenderBase
{
  val NO_REPLY_ARTY_MONKEYS = "no-reply@artymonkeys.co.uk"

  val artyInfoAddress = Play.current.configuration.getString("arty.info.email")
  val INFO_ARTY_MONKEYS = artyInfoAddress.getOrElse("info@artymonkeys.co.uk")

  def sendEmails(customer: Customer): Boolean

  def handleEmailSending(customer: Customer): Result =
  {
    val weShouldSendEmails = Play.current.configuration.getBoolean("send.emails")
    if (weShouldSendEmails.getOrElse(true)) {
      if (sendEmails(customer)) {
        Ok(views.html.errorsendingemail())
      } else {
        Ok(views.html.thankyou())
      }
    } else {
      Ok(views.html.thankyou())
    }
  }

  def buildCustomerFromRequest(request: Request[AnyContent]): Customer =
  {
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
    if (formDetailsOption.isDefined) {
      val formDetails = formDetailsOption.get
      for (key <- formDetails.keys) {
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

    val monkeyTuple = customerMonkeyNames zip customerMonkeyDobs
    val monkeysList = for (monkey <- monkeyTuple) yield {
      Monkey(Some(monkey._1), Some(monkey._2))
    }

    new Customer(
      Some(customerName),
      EmailWrapper(Some(customerEmail)),
      PhoneWrapper(Some(customerPhone)),
      new CustomerPreferences(Some(customerPreferredVenue), Some(customerPreferredClassTime)),
      monkeysList.toList,
      new HowDidYouHear(Some(customerHowDidYouHear), Some(customerHowDidYouHearExtra)),
      Some(customerMessage),
      Some(null)
    )
  }

}
