package controllers.model

import controllers.dao.{PhoneWrapper, EmailWrapper, NameWrapper}

case class Customer(nameWrapper: NameWrapper,
                   emailWrapper: EmailWrapper,
                   phoneWrapper: PhoneWrapper
                     )

