package controllers.service.news

import controllers.dao.news.NewsHomeRepositoryComponent
import model.NewsHomeItem

trait DefaultNewsHomeServiceComponent extends NewsHomeServiceComponent {
  this: NewsHomeRepositoryComponent =>

  def newsHomeService = new DefaultNewsHomeService

  class DefaultNewsHomeService extends NewsHomeService {
    def getLastNewsItems(numberItemsToReturn: Int) = newsHomeLocator.getLastNewsItems(numberItemsToReturn)

    def save(newsItem: NewsHomeItem) {
      newsHomeUpdater.save(newsItem)
    }
  }
}
