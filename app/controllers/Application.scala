package controllers

import controllers.dao.{CustomerDao, UsersDao}
import controllers.email.{EmailInterestRegistered, EmailEnquiry}
import play.api.Logger
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.mvc._

import scala.concurrent.Future

object Application extends Controller
with EmailInterestRegistered with EmailEnquiry with Secured {

  val usersDao = new UsersDao()
  val customersDao = new CustomerDao()

  def index = SecureAction {
    Ok(views.html.index("Your new application is ready."))
  }

  def about = SecureAction {
    Ok(views.html.about())
  }

  def classes = SecureAction {
    Ok(views.html.classes())
  }

  def contacts = SecureAction {
    Ok(views.html.contacts())
  }

  def skills = SecureAction {
    Ok(views.html.skills())
  }

  def monkeynews = SecureAction {
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

  lazy val signupForm = Form(
    tuple(
      "email" -> text,
      "password" -> text
    ) verifying("Invalid user or password", result => result match {
      case (email, password)   => {
        if ( usersDao.addUser(email, password )) {
          true
        } else {
          false
        }
      }
      case _ => false
    })
  )

  override def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Application.login())

  def doLogin() = Action { implicit request =>
    Logger.info("Authenticating user")
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.login(formWithErrors, routes.Application.login())),
      user => Redirect(routes.Application.index()).withSession("user" -> user._1)
    )
  }

  def doSignup() = Action { implicit request =>
    Logger.info("Checking user signup details okay")
    signupForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.signup(formWithErrors, routes.Application.signup())),
      user => Redirect(routes.Application.index()).withSession("user" -> user._1)
    )
  }

  def login = Action { implicit request =>
    Ok(views.html.login(loginForm, routes.Application.doLogin()))
  }

  def signup = Action { implicit request =>
    Ok(views.html.signup(signupForm, routes.Application.doSignup()))
  }

  def logout = Action {
    Redirect(routes.Application.index()).withNewSession.flashing(
      "success" -> "You've been logged out"
    )
  }

  def customers = IsAuthenticated { username => request =>
    val customers = customersDao.retrieveAllCustomers()
    Ok(views.html.showCustomers(customers))
  }

}

object SecureAction extends ActionBuilder[Request] {
  def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
    val secureRequest = new SecureRequest[A](request)
    val httpsPortVal = System.getProperty("https.port")
    val httpsPort = if (httpsPortVal != null) httpsPortVal else "443"
    if ( request.secure ) {
      block(secureRequest)
    } else {
      Future(Results.Redirect(s"https://${request.domain}:" + httpsPort + s"${request.uri}"))
    }
  }
}

class SecureRequest[A](request: Request[A]) extends WrappedRequest[A](request) {
  override def secure = true
}

