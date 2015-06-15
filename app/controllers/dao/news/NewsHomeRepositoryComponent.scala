package controllers.dao.news

import model.NewsHomeItem

trait NewsHomeRepositoryComponent {
  def newsHomeLocator : NewsHomeLocator
  def newsHomeUpdater : NewsHomeUpdater

  trait NewsHomeLocator {
    def getLastNewsItems(numberOfNewsItemsToShow: Int): Seq[NewsHomeItem]
  }

  trait NewsHomeUpdater {
    def save(newsItem: NewsHomeItem)
  }

}
