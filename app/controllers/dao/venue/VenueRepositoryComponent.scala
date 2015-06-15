package controllers.dao.venue

trait VenueRepositoryComponent {
  def venueLocator: VenueLocator

  trait VenueLocator {
    def retrieveVenueClassTimes(venue: String): List[String]
  }
}
