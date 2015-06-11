package controllers

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoCollection
import controllers.dao.user.UserRepositoryMongoComponent
import controllers.service.user.DefaultUserServiceComponent

object ApplicationCake {
  val userServiceComponent = new DefaultUserServiceComponent with UserRepositoryMongoComponent {
    override val userCollection: MongoCollection = MongoConnection()("artymonkeys")("users")
  }
  val userService = userServiceComponent.userService
}
