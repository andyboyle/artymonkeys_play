package controllers

import play.api.Logger
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._


object Application extends Controller
with EmailInterestRegistered with EmailEnquiry with Secured {

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

  //  def monkeynews = IsAuthenticated { username => request =>
  //    Ok(views.html.monkeynews())
  //  }
  def monkeynews = Action {
    Ok(views.html.monkeynews())
  }

  lazy val loginForm = Form(
    tuple(
      "user" -> text,
      "password" -> text
    ) verifying("Invalid user or password", result => result match {
      case (user, password) if user == "user" && password == "password" => true
      case _ => false
    })
  )

  override def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Application.login())

  def doLogin() = Action { implicit request =>
    Logger.info("Authenticating user")
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.login(formWithErrors, routes.Application.login)),
      user => Redirect(routes.Application.index()).withSession("user" -> user._1)
    )
  }

  def login = Action { implicit request =>
    Ok(views.html.login(loginForm, routes.Application.doLogin()))
  }

  def logout = Action {
    Redirect(routes.Application.index()).withNewSession.flashing(
      "success" -> "You've been logged out"
    )
  }

}