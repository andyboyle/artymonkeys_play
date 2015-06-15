package controllers.service.venue

trait VenueServiceComponent {
  def venueService: VenueService

  trait VenueService {
    def retrieveVenueClassTimes(venueName: String) : List[String]
  }
}
