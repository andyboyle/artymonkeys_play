package controllers

import controllers.dao.{NewsHomeDao, CustomerDao, UserDao}
import controllers.email.{EmailEnquiry, EmailInterestRegistered}
import play.api.mvc._

object Application extends Controller
with EmailInterestRegistered with EmailEnquiry with Secured  {

  val usersDao = new UserDao()
  val customersDao = new CustomerDao()
  val newsHomeDao = new NewsHomeDao()

  def index = SecureAction { request =>
    println("Index Success: " + request.cookies.get("gtoken"))
    Ok(views.html.index(isAdminUser(request)))
  }

  def about = SecureAction { request =>
    println("About Success: " + request.cookies.get("gtoken"))
    Ok(views.html.about(isAdminUser(request)))
  }

  def classes = SecureAction { request =>
    println("Classes Success: " + request.cookies.get("gtoken"))
    Ok(views.html.classes(isAdminUser(request)))
  }

  def contacts = SecureAction { request =>
    println("Contacts Success: " + request.cookies.get("gtoken"))
    Ok(views.html.contacts(isAdminUser(request)))
  }

  def skills = SecureAction { request =>
    println("Skills Success: " + request.cookies.get("gtoken"))
    Ok(views.html.skills(isAdminUser(request)))
  }

  def monkeynews = SecureAction { request =>
    println("Monkey News Success: " + request.cookies.get("gtoken"))
    Ok(views.html.monkeynews(isAdminUser(request)))
  }

  def oauth2call = SecureAction { request =>
    println("Oauth 2 callback Success: " + request.session)
    Ok(views.html.oauth())
  }

  def loginSuccess = SecureAction { request =>
    if ( isAdminUser(request)) {
      Ok(views.html.loginsuccess(isAdminUser(request)))
    } else {
      Unauthorized(views.html.unauthorised())
    }
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

  def unauthorised = SecureAction {
    Ok(views.html.unauthorised())
  }

  def managenews = SecureAction { request =>
    if (isAdminUser(request)) {
      val newsItems = newsHomeDao.getLastNewsItems()
      Ok(views.html.managenews(true, newsItems))
    } else {
      Unauthorized(views.html.unauthorised())
    }
  }

  def customers = SecureAction { request =>
    if (isAdminUser(request)) {
      val allCustomers = customersDao.retrieveAllCustomers()
      Ok(views.html.showCustomers(true, allCustomers))
    } else {
      Unauthorized(views.html.unauthorised())
    }
  }

  private def isAdminUser(request: Request[AnyContent]): Boolean = {
    val user = userIdFromHeader(request)
    val users = usersDao.retrieveAllUsers()
    if (user.isDefined && users.exists(theuser => theuser.id == user.get.toString.replace("\"", ""))) {
      true
    } else {
      false
    }
  }

}
