package controllers.email

import controllers.dao.CustomerDao

trait EmailSenderBase {
  val customerDao = new CustomerDao
  val NO_REPLY_ARTY_MONKEYS = "no-reply@artymonkeys.co.uk"
  val INFO_ARTY_MONKEYS = "andyjcboyle@gmail.com"
}
