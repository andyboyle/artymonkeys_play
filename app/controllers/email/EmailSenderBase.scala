package controllers.email

import play.api.Play

trait EmailSenderBase {
  val NO_REPLY_ARTY_MONKEYS = "no-reply@artymonkeys.co.uk"

  val artyInfoAddress = Play.current.configuration.getString("arty.info.email")
  val INFO_ARTY_MONKEYS = artyInfoAddress.getOrElse("info@artymonkeys.co.uk")
}
