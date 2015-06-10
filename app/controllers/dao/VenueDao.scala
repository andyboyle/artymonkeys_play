package controllers.dao

import com.mongodb.casbah.Imports._

class VenueDao {
  val venueCollection = MongoConnection()("artymonkeys")("venue")

  def getVenueTimes(venueName: String ) : List[String] = {
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
