package controllers.dao.user

import com.mongodb.casbah.Imports._
import controllers.dao.user.UserDaoTrait
import model.{EmailWrapper, User}

class UserDao extends UserDaoTrait {

  val userCollection = MongoConnection()("artymonkeys")("users")

  override def retrieveAllUsers(): Seq[User] = {
    println("Retrieving users now ...")
    val userCursor = userCollection.find()
    val allusers = for {userObj <- userCursor} yield {
      println("the user is : " + userObj)
      val userId = userObj.get("_id").asInstanceOf[String]
      val userEmail = EmailWrapper(Some(userObj.get("email").asInstanceOf[String]))
      User(userId, userEmail)
    }
    allusers.toSeq
  }

}
