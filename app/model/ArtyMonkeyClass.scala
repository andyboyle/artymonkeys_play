package model

case class ArtyMonkeyClassBluePrint(
                                     className: Option[String],
                                     classDescription: Option[String]
                                     )

case class ArtyMonkeyClassInstance(
                                    classBluePrint: Option[ArtyMonkeyClassBluePrint],
                                    location: Option[String],
                                    time: Option[String]
                                    )
