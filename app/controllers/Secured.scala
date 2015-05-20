package controllers

import com.google.common.io.BaseEncoding
import play.api.libs.json.Json
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.Future


trait Secured {
  self: Controller =>

  /**
   * Retrieve the connected user id.
   */
  def userIdFromHeader(request: RequestHeader) : Option[String] = {
    val gtokenOption = request.cookies.get("gtoken")
    if (gtokenOption.isDefined) {
      val gtoken = gtokenOption.get.value
      println("token '" + gtoken + "'")

      val jwtParts = gtoken.split("\\.")
      val jwtHeader = jwtParts(0)
      val jwtBody = jwtParts(1)
      val jwtSigning = jwtParts(2)

      val decodedHeaderBytes = BaseEncoding.base64().decode(jwtHeader)
      val decodedHeaderString = new String(decodedHeaderBytes, "UTF-8")

      val decodedBodyBytes = BaseEncoding.base64().decode(jwtBody)
      val jwtBodyJson = Json.parse(decodedBodyBytes)

      println("Header = '" + decodedHeaderString + "'")
      println("Body = '" + jwtBodyJson + "'")
      println("Email = " + jwtBodyJson.\("email"))
      val userId = jwtBodyJson.\("user_id")
      println("Id = " + userId)
      Some(userId.toString())
    } else {
      None
    }
  }

}

object SecureAction extends ActionBuilder[Request] {
  def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
    val secureRequest = new
        SecureRequest[A](request)
    val httpsPortVal = System.getProperty("https.port")
    val httpsPort = if (httpsPortVal != null) httpsPortVal else "443"
    if (request.secure) {
      block(secureRequest)
    } else {
      Future(Results.Redirect(s"https://${request.domain}:" + httpsPort + s"${request.uri}"))
    }
  }
}

class SecureRequest[A](request: Request[A]) extends WrappedRequest[A](request) {
  override def secure = true
}


