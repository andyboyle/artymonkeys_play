package controllers

import controllers.dao.{CustomerDao, UserDao}
import controllers.email.{EmailEnquiry, EmailInterestRegistered}
import play.api.mvc._

object Application extends Controller
with EmailInterestRegistered with EmailEnquiry with Secured {

  val usersDao = new UserDao()
  val customersDao = new CustomerDao()

  def index = SecureAction { request =>
    println("Index Success: " + request.cookies.get("gtoken"))
    Ok(views.html.index("Your new application is ready."))
  }

  def about = SecureAction { request =>
    println("About Success: " + request.cookies.get("gtoken"))
    Ok(views.html.about())
  }

  def classes = SecureAction { request =>
    println("Classes Success: " + request.cookies.get("gtoken"))
    Ok(views.html.classes())
  }

  def contacts = SecureAction { request =>
    println("Contacts Success: " + request.cookies.get("gtoken"))
    Ok(views.html.contacts())
  }

  def skills = SecureAction { request =>
    println("Skills Success: " + request.cookies.get("gtoken"))
    Ok(views.html.skills())
  }

  def monkeynews = SecureAction { request =>
    println("Monkey News Success: " + request.cookies.get("gtoken"))
    Ok(views.html.monkeynews())
  }

  def oauth2call = SecureAction { request =>
    println("Oauth 2 callback Success: " + request.session)
    Ok(views.html.oauth())
  }

  def loginSuccess = SecureAction { request =>
    // TODO: Stop login here for invalid users
    println("Login Success: " + request.cookies.get("gtoken"))
    val gtokenOption = request.cookies.get("gtoken")
    if (gtokenOption.isDefined) {
      val gtoken = gtokenOption.get

    }
    Ok(views.html.loginsuccess())
  }

  def logoutSuccess = SecureAction {
//    Ok(views.html.logoutsuccess())
    Redirect(routes.Application.index()).withNewSession.flashing(
      "success" -> "You've been logged out"
    )
  }

  def login = SecureAction { implicit request =>
    Ok(views.html.login())
  }

  def logout = SecureAction {
    Redirect(routes.Application.index()).withNewSession.flashing(
      "success" -> "You've been logged out"
    )
  }

  def customers = SecureAction { request =>
    val user = userIdFromHeader(request)
    val users = usersDao.retrieveAllUsers()
    if (user.isDefined && users.exists(theuser => theuser.id == user.get.toString.replace("\"", ""))) {
      val allCustomers = customersDao.retrieveAllCustomers()
      Ok(views.html.showCustomers(allCustomers))
    } else {
      Unauthorized("You do not have access to this resource")
    }
  }

}
