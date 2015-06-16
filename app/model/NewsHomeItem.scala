package model

import org.bson.types.ObjectId
import org.joda.time.LocalDate

case class NewsHomeItem(id: ObjectId,
                        posted: LocalDate,
                        headline: String,
                        newsDetails: Seq[String])
