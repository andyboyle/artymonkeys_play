package controllers.dao

import com.mongodb.BasicDBObjectBuilder
import com.mongodb.casbah.Imports._

case class NameWrapper(name: Option[String])

case class EmailWrapper(email: Option[String])

case class PhoneWrapper(phone: Option[String])

class CustomerDao {
  val mongoClient = MongoClient("localhost", 27017)
  val artyMonkeysDatabase = mongoClient("artymonkeys")
  private val customersCollection = artyMonkeysDatabase.getCollection("customers")

  def addUser(email: EmailWrapper, name: NameWrapper, phone: PhoneWrapper): Boolean = {
    val id =
      email.email.getOrElse("NoEmail") + "_" +
      name.name.getOrElse("NoName") + "_" +
      phone.phone.getOrElse("NoPhone")

    val customerDbObjBuilder = new BasicDBObjectBuilder()
      .add("_id", id)
      .add("name", name.name)
      .add("phone", phone.phone)
      .add("email", email.email)

    try {
      customersCollection.insert(customerDbObjBuilder.get)
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


}
