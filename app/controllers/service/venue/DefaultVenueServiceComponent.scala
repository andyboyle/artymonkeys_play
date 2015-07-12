package controllers.service.venue

import controllers.dao.venue.VenueRepositoryComponent
import model.Venue

trait DefaultVenueServiceComponent extends VenueServiceComponent {
  this: VenueRepositoryComponent =>

  def venueService = new DefaultVenueService

  class DefaultVenueService extends VenueService {

    override def retrieveVenueClassTimes(venueName: String): List[String] = {
      venueLocator.retrieveVenueClassTimes(venueName)
    }

    override def retrieveAllVenues(): List[Venue] = {
      venueLocator.retrieveAllVenues()
    }
  }

}
