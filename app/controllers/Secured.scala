package controllers

import play.api._
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._


trait Secured {
  self: Controller =>

  /**
   * Retrieve the connected user id.
   */
  def username(request: RequestHeader) = request.session.get("user")

  /**
   * Redirect to login if the use in not authorized.
   */
  def onUnauthorized(request: RequestHeader): Result

  def IsAuthenticated(f: => String => Request[AnyContent] => Result) =
    Security.Authenticated(username, onUnauthorized) { user =>
      Action(request => f(user)(request))
    }
}
