package controllers.dao.venue

import model.Venue

trait VenueRepositoryComponent {
  def venueLocator: VenueLocator

  trait VenueLocator {
    def retrieveVenueClassTimes(venue: String): List[String]

    def retrieveAllVenues(): List[Venue]
  }
}
