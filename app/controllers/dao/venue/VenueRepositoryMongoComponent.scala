package controllers.dao.venue

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoCollection

trait VenueRepositoryMongoComponent extends VenueRepositoryComponent {

  val venueCollection: MongoCollection

  def venueLocator = new VenueLocatorMongoDb(venueCollection)

  class VenueLocatorMongoDb(val collection: MongoCollection) extends VenueLocator {
    def retrieveVenueClassTimes(venueName: String) = {
      println("And we're totally caking once more!")
      val query = MongoDBObject("shortname" -> venueName)
      val result = venueCollection.findOne(query)
      if ( result.isDefined ) {
        val times = result.get.get("defaultClassTimes").asInstanceOf[com.mongodb.BasicDBList]
        times.toList.map( x => x.toString )
      } else {
        List()
      }
    }
  }

}
