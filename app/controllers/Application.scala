package controllers

import play.api.mvc._

object Application extends Controller with EmailInterestRegistered with EmailEnquiry {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def about = Action {
    Ok(views.html.about())
  }

  def classes = Action {
    Ok(views.html.classes())
  }

  def contacts = Action {
    Ok(views.html.contacts())
  }

  def skills = Action {
    Ok(views.html.skills())
  }

}