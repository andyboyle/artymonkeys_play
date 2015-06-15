package model

import controllers.ApplicationCake

object NewsHomeHelper {

  def getLastNewsItems(): Seq[NewsHomeItem] = {
    ApplicationCake.newsHomeService.getLastNewsItems(5)
  }

}
