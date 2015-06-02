package controllers.dao

import com.mongodb.casbah.Imports._
import model.{EmailWrapper, User}

class UserDao {

  val userCollection = MongoConnection()("artymonkeys")("users")

  def retrieveAllUsers(): Seq[User] = {
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
