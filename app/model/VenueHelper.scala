package model

import controllers.dao.VenueDao

class VenueHelper {
  val venueDao = new VenueDao()

  def getVenueTimesHtml(venueName: String) : String = {
    val times = venueDao.getVenueTimes(venueName)
    val htmlOptions = times.map( x => "<option value='" + x.replaceAll(" ","") + "'>" + x +"</option>" )

    val htmlToReturn =
    "<div id=\"monkeyclasstimesdiv\" class=\"formelement\">\n" +
      "Time:\n" +
      "<select id=\"monkeyclasstimes\" class=\"howdidyouhear\" name=\"time\" required>\n" +
        "<option selected disabled hidden value=''></option>\n" +
          htmlOptions +
      "</select>\n" +
    "</div>\n"

    return htmlToReturn
  }

}
