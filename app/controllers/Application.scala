package controllers

import play.api.libs.concurrent.Execution.Implicits._
import play.Logger
import play.api.mvc._

import scala.concurrent.Future

object Application extends Controller with EmailInterestRegistered with EmailEnquiry {

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

  def artyparties = SecureAction {
    Ok(views.html.artyparties())
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

