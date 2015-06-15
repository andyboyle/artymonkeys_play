package controllers.dao.user

import com.mongodb.casbah.MongoCollection
import model.{EmailWrapper, User}

trait UserRepositoryMongoComponent extends UserRepositoryComponent {

  val userCollection: MongoCollection

  def userLocator = new UserLocatorMongoDb(userCollection)

  def userUpdater = new UserUpdaterMongoDb(userCollection)

  class UserLocatorMongoDb(val collection: MongoCollection) extends UserLocator {
    def retrieveAllUsers = {
      val userCursor = collection.find()
      val allUsers = for {userObj <- userCursor} yield {
        println("We're caking it all up here man! The user is : " + userObj)
        val userId = userObj.get("_id").asInstanceOf[String]
        val userEmail = EmailWrapper(Some(userObj.get("email").asInstanceOf[String]))
        User(userId, userEmail)
      }
      allUsers.toSeq
    }
  }

  class UserUpdaterMongoDb(val collection: MongoCollection) extends UserUpdater {
    def save(user: User) {
    }
  }

}