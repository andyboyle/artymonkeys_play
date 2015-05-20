package controllers.model

import controllers.dao.EmailWrapper

case class User(id: String, email: EmailWrapper)
