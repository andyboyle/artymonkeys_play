package controllers.dao

import com.mongodb.BasicDBObject
import com.mongodb.casbah.Imports._
import controllers.model.{PhoneWrapper, EmailWrapper, NameWrapper, Customer}

class CustomerDao {

  val customersCollection = MongoConnection()("artymonkeys")("customers")

  def addUser(email: EmailWrapper, knownAs: Option[String], phone: PhoneWrapper): Boolean = {

    try {

      val names = MongoDBObject(
        "knownas" -> knownAs.getOrElse("No Name"),
        "firstname" -> "",
        "middlenames" -> "",
        "surname" -> ""
      )

      val phonenumbers = MongoDBObject("best" -> phone.phone)

      val customerObj = MongoDBObject(
        "name" -> names,
        "phone" -> phonenumbers,
        "email" -> email.email
      )
      customersCollection += customerObj
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

  def retrieveAllCustomers(): Seq[Customer] = {
    println("Retrieving customers now ...")
    val customersCursor = customersCollection.find()
    val allCustomers = for {customerObj <- customersCursor} yield {
      println("the cust is : " + customerObj)
      val customerName =
        Some(customerObj.get("name").asInstanceOf[BasicDBObject].get("knownas").asInstanceOf[String])
      val customerEmail = EmailWrapper(Some(customerObj.get("email").asInstanceOf[String]))
      val customerPhone =
        PhoneWrapper(Some(customerObj.get("phone").asInstanceOf[BasicDBObject].get("best").asInstanceOf[String]))
      Customer(customerName, customerEmail, customerPhone)
    }
    allCustomers.toSeq
  }


}
