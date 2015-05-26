package controllers.model

case class NameWrapper(knownAs: Option[String],
                       firstName: Option[String],
                       middleNames: Option[String],
                       surname: Option[String] )

case class EmailWrapper(email: Option[String])

case class PhoneWrapper(phone: Option[String])
