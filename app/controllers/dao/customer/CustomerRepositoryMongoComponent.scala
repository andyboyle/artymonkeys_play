package controllers.dao.customer

import com.mongodb.BasicDBObject
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoCollection
import model._

trait CustomerRepositoryMongoComponent extends CustomerRepositoryComponent {

  val customerCollection: MongoCollection

  def customerLocator = new CustomerLocatorMongoDb(customerCollection)

  def customerUpdater = new CustomerUpdaterMongoDb(customerCollection)

  class CustomerLocatorMongoDb(val customersCollection: MongoCollection) extends CustomerLocator {

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
        val preferenceLocation = customerObj.get("preferences").asInstanceOf[BasicDBObject].get("location").asInstanceOf[String]
        val preferenceTime = customerObj.get("preferences").asInstanceOf[BasicDBObject].get("time").asInstanceOf[String]
        val monkeysList = customerObj.get("monkeys").asInstanceOf[BasicDBList]
        val howdidyouhearCategory = customerObj.get("howdidyouhear").asInstanceOf[BasicDBObject].get("category").asInstanceOf[String]
        val howdidyouhearExtra = customerObj.get("howdidyouhear").asInstanceOf[BasicDBObject].get("extra").asInstanceOf[String]
        val message = customerObj.get("message").asInstanceOf[String]

        val monkeys = for {monkey <- monkeysList
                           name = monkey.asInstanceOf[BasicDBList].get(0).asInstanceOf[String]
                           if name != null && name != ""
        } yield {
            new Monkey(
              Some(monkey.asInstanceOf[BasicDBList].get(0).asInstanceOf[String]),
              Some(monkey.asInstanceOf[BasicDBList].get(1).asInstanceOf[String]))
          }

        Customer(customerName, customerEmail, customerPhone,
          new CustomerPreferences(Some(preferenceLocation), Some(preferenceTime)),
          monkeys,
          new HowDidYouHear(Some(howdidyouhearCategory), Some(howdidyouhearExtra)),
          Some(message)
        )
      }
      allCustomers.toSeq
    }

  }

  class CustomerUpdaterMongoDb(val customerCollection: MongoCollection) extends CustomerUpdater {
    def save(customer: Customer): Unit = {
      try {

        val names = MongoDBObject(
          "knownas" -> customer.knownas.getOrElse("No Name"),
          "firstname" -> "",
          "middlenames" -> "",
          "surname" -> ""
        )

        val phonenumbers = MongoDBObject("best" -> customer.phoneWrapper.phone.getOrElse("No Phone"))

        val preferenceDetail = MongoDBObject(
          "location" -> customer.customerPreferences.location.getOrElse("No Location Preference"),
          "time" -> customer.customerPreferences.time.getOrElse("No Time Preference")
        )

        val customerObj = MongoDBObject(
          "name" -> names,
          "phone" -> phonenumbers,
          "email" -> customer.emailWrapper.email.getOrElse("No Email"),
          "preferences" -> preferenceDetail,
          "monkeys" -> customer.monkeys,
          "howdidyouhear" ->
            new BasicDBObject("category", customer.howDidYouHear.category.getOrElse("No Category"))
              .append("extra", customer.howDidYouHear.extraText.getOrElse("No Category")),
          "message" -> customer.message
        )

        customerCollection += customerObj
      }
      catch {
        case e: Exception => {
          println("Error is : " + e)
          println("Error is : " + e.getMessage)
        }
      }
    }
  }


}
