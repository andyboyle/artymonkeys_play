package controllers.dao.venue

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoCollection
import model.Venue

trait VenueRepositoryMongoComponent extends VenueRepositoryComponent
{

  val venueCollection: MongoCollection

  def venueLocator = new VenueLocatorMongoDb(venueCollection)

  class VenueLocatorMongoDb(val collection: MongoCollection) extends VenueLocator
  {
    def retrieveVenueClassTimes(venueName: String) =
    {
      println("And we're totally caking once more!")
      val query = MongoDBObject("shortname" -> venueName)
      val result = venueCollection.findOne(query)
      if (result.isDefined) {
        val times = result.get.get("defaultClassTimes").asInstanceOf[com.mongodb.BasicDBList]
        times.toList.map(x => x.toString)
      } else {
        List()
      }
    }

    override def retrieveAllVenues(): List[Venue] =
    {
      val result = venueCollection.find().toList
      for (res <- result) yield {

        val firstLines: BasicDBList = res.get("address").asInstanceOf[BasicDBObject].get("firstLines").asInstanceOf[BasicDBList]
        val firstLinesList = scala.collection.mutable.ListBuffer.empty[String]
        for (lineAny <- firstLines)
        {
         firstLinesList += lineAny.toString
        }

        val classTimes: BasicDBList = res.get("defaultClassTimes").asInstanceOf[BasicDBList]
        val classTimesList = scala.collection.mutable.ListBuffer.empty[String]
        for (classTime <- classTimes)
        {
          classTimesList += classTime.toString
        }

        new Venue(
          res.get("_id").asInstanceOf[org.bson.types.ObjectId].toString,
          res.get("name").asInstanceOf[String],
          res.get("shortname").asInstanceOf[String],
          firstLinesList,
          res.get("address").asInstanceOf[BasicDBObject].get("town").asInstanceOf[String],
          res.get("address").asInstanceOf[BasicDBObject].get("county").asInstanceOf[String],
          res.get("address").asInstanceOf[BasicDBObject].get(".postcode").asInstanceOf[String],
          res.get("telephone").asInstanceOf[String],
          classTimesList,
          res.get("website").asInstanceOf[String],
          res.get("facebook").asInstanceOf[String]
        )
      }
    }
  }

}
