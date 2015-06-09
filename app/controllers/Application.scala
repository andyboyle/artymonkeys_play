package controllers

import controllers.dao.{VenueDao, NewsHomeDao, CustomerDao, UserDao}
import controllers.email.{EmailEnquiry, EmailInterestRegistered}
import model.VenueHelper
import play.Routes
import play.api.mvc._


object Application extends Controller
with EmailInterestRegistered with EmailEnquiry with Secured {

  val usersDao = new UserDao()
  val customersDao = new CustomerDao()
  val newsHomeDao = new NewsHomeDao()

  def index = SecureAction { request =>
    Ok(views.html.index(isAdminUser(request), newsHomeDao.getLastNewsItems()))
  }

  def about = SecureAction { request =>
    Ok(views.html.about(isAdminUser(request)))
  }

  def classes = SecureAction { request =>
    Ok(views.html.classes(isAdminUser(request)))
  }

  def contacts = SecureAction { request =>
    Ok(views.html.contacts(isAdminUser(request)))
  }

  def ajaxTest(classLoc: String) = Action { request =>
    Ok(new VenueHelper().getVenueTimesHtml(classLoc))
  }

  def locationClassTimes: List[String] = {
    List("Hello1", "Hello2")
  }

  def skills = SecureAction { request =>
    Ok(views.html.skills(isAdminUser(request)))
  }

  def monkeynews = SecureAction { request =>
    Ok(views.html.monkeynews(isAdminUser(request)))
  }

  def oauth2call = SecureAction { request =>
    Ok(views.html.oauth())
  }

  def loginSuccess = SecureAction { request =>
    if (isAdminUser(request)) {
      Ok(views.html.loginsuccess(isAdminUser(request)))
    } else {
      Unauthorized(views.html.unauthorised())
    }
  }

  def logoutSuccess = SecureAction {
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
      Ok(views.html.managenews(true, newsHomeDao.getLastNewsItems()))
    } else {
      Unauthorized(views.html.unauthorised())
    }
  }

  def managenewspost = SecureAction { request =>
    if (isAdminUser(request)) {
      Ok(views.html.managenews(true, newsHomeDao.getLastNewsItems()))
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
