package model

case class Venue(id: String,
                name: String,
                shortname: String,
                addressFirstLines: Seq[String],
                addressTown: String,
                addressCounty: String,
                addressPostcode: String,
                telephone: String,
                classTimes: Seq[String],
                website: String,
                facebook: String
                  )

