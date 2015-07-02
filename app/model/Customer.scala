package model

import org.joda.time.LocalDateTime
import org.joda.time.format.{DateTimeFormatter, DateTimeFormat, PeriodFormatterBuilder, PeriodFormatter}

case class Customer(knownas: Option[String],
                    emailWrapper: EmailWrapper,
                    phoneWrapper: PhoneWrapper,
                    customerPreferences: CustomerPreferences,
                    monkeys: Seq[Monkey],
                    howDidYouHear: HowDidYouHear,
                    message: Option[String],
                    enquiryDateTime: Option[LocalDateTime]
                     )
{
  def prettyPrintEnquiryDate(): String =
  {
    val datefmt: DateTimeFormatter = DateTimeFormat.forPattern("d MMM");

    val timefmt: DateTimeFormatter = DateTimeFormat.forPattern("hh:mma");
    enquiryDateTime.get.toLocalTime.toString(timefmt) + ", " + enquiryDateTime.get.toLocalDate.toString(datefmt)
  }
}

case class CustomerPreferences(location: Option[String],
                               time: Option[String]
                                )

case class HowDidYouHear(
                          category: Option[String],
                          extraText: Option[String]
                          )

case class Monkey(name: Option[String],
                  dob: Option[String]
                   )
