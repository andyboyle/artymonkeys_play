package controllers

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoCollection
import controllers.dao.news.NewsHomeRepositoryMongoComponent
import controllers.dao.user.UserRepositoryMongoComponent
import controllers.dao.venue.VenueRepositoryMongoComponent
import controllers.service.news.DefaultNewsHomeServiceComponent
import controllers.service.user.DefaultUserServiceComponent
import controllers.service.venue.DefaultVenueServiceComponent

object ApplicationCake {

  /////
  val userServiceComponent = new DefaultUserServiceComponent with UserRepositoryMongoComponent {
    override val userCollection: MongoCollection = MongoConnection()("artymonkeys")("users")
  }

  val userService = userServiceComponent.userService
  /////

  /////
  val venueServiceComponent = new DefaultVenueServiceComponent with VenueRepositoryMongoComponent {
    override val venueCollection: MongoCollection = MongoConnection()("artymonkeys")("venue")
  }

  val venueService = venueServiceComponent.venueService
  /////

  /////
  val newsHomeServiceComponent = new DefaultNewsHomeServiceComponent with NewsHomeRepositoryMongoComponent {
    override val newsHomeCollection: MongoCollection = MongoConnection()("artymonkeys")("newshome")
  }

  val newsHomeService = newsHomeServiceComponent.newsHomeService
  /////
}
