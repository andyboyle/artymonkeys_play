package controllers.model

case class Customer(knownas: Option[String],
                    emailWrapper: EmailWrapper,
                    phoneWrapper: PhoneWrapper,
                    customerPreferences: CustomerPreferences,
                    monkeys: Seq[Monkey],
                    howDidYouHear: HowDidYouHear,
                    message: Option[String]
                     )

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
