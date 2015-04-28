package controllers.dao

import java.io.UnsupportedEncodingException
import java.security.{SecureRandom, MessageDigest, NoSuchAlgorithmException}
import java.util.Random

import com.mongodb.{BasicDBObjectBuilder, DBCollection}
import sun.misc.BASE64Encoder
import com.mongodb.casbah.Imports._

class UsersDao {
  private var usersCollection: DBCollection = null
  private var random: Random = new SecureRandom

  val mongoClient = MongoClient("localhost", 27017)
  val artyMonkeysDatabase = mongoClient("artymonkeys")
  usersCollection = artyMonkeysDatabase.getCollection("users")

  def addUser(email: String, password: String): Boolean = {
    val passwordHash = makePasswordHash(password, Integer.toString(random.nextInt))
    val usernamePasswordDbObjBuilder = new BasicDBObjectBuilder()
      .add("_id", email)
      .add("password", passwordHash)

    if (email != null && !(email == "")) {
      usernamePasswordDbObjBuilder.add("email", email)
    }

    try {
      usersCollection.insert(usernamePasswordDbObjBuilder.get)
      true
    }
    catch {
      case e: Exception => {
        println("Error is : " + e)
        println("Error is : " + e.getMessage)
        false
      }
    }
  }

  private def makePasswordHash(password: String, salt: String): String = {
    val saltedAndHashed = password + "," + salt
    val digest = MessageDigest.getInstance("MD5")
    digest.update(saltedAndHashed.getBytes)
    val encoder = new BASE64Encoder
    val hashedBytes = new String(digest.digest, "UTF-8").getBytes
    encoder.encode(hashedBytes) + "," + salt
  }

}
