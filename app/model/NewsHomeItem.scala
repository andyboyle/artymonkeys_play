package model


import org.bson.types.ObjectId
import org.joda.time.{LocalDateTime, LocalDate}

case class NewsHomeItem(id: ObjectId,
                        posted: LocalDateTime,
                        headline: String,
                        newsDetails: Seq[String]
                         )
