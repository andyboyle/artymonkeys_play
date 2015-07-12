package controllers.service.venue

import model.Venue

trait VenueServiceComponent {
  def venueService: VenueService

  trait VenueService {
    def retrieveVenueClassTimes(venueName: String) : List[String]
    def retrieveAllVenues() : List[Venue]
  }
}
