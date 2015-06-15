package controllers.service.news

import model.NewsHomeItem

trait NewsHomeServiceComponent {
  def newsHomeService: NewsHomeService

  trait NewsHomeService {
    def getLastNewsItems(numberOfNewsItemsToShow: Int): Seq[NewsHomeItem]

    def save(newsItem: NewsHomeItem)
  }

}
