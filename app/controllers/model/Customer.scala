package controllers.model

case class Customer(knownas: Option[String],
                   emailWrapper: EmailWrapper,
                   phoneWrapper: PhoneWrapper
                     )

