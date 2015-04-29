package controllers.dao

import com.mongodb.casbah.Imports._
import controllers.model.Customer

case class NameWrapper(name: Option[String])

case class EmailWrapper(email: Option[String])

case class PhoneWrapper(phone: Option[String])

class CustomerDao {

  val customersCollection = MongoConnection()("artymonkeys")("customers")

  def addUser(email: EmailWrapper, name: NameWrapper, phone: PhoneWrapper): Boolean = {
    val id = email.email.getOrElse("NoEmail")

    try {
      val customerObj = MongoDBObject(
        "_id" -> id,
        "name" -> name.name,
        "phone" -> phone.phone,
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
      val customerName = NameWrapper(Some(customerObj.get("name").asInstanceOf[String]))
      val customerEmail = EmailWrapper(Some(customerObj.get("email").asInstanceOf[String]))
      val customerPhone = PhoneWrapper(Some(customerObj.get("phone").asInstanceOf[String]))
      Customer(customerName, customerEmail, customerPhone)
    }
    allCustomers.toSeq
  }


}
