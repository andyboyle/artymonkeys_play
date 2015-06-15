package controllers.dao.news

import java.util.Date

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.MongoCollection
import model.NewsHomeItem
import org.joda.time.LocalDate

trait NewsHomeRepositoryMongoComponent extends NewsHomeRepositoryComponent {

  val newsHomeCollection: MongoCollection

  def newsHomeLocator = new NewsHomeLocatorMongoDb(newsHomeCollection)

  def newsHomeUpdater = new NewsHomeUpdaterMongoDb(newsHomeCollection)


  class NewsHomeLocatorMongoDb(val collection: MongoCollection) extends NewsHomeLocator {

    def getLastNewsItems(numberOfNewsItemsToShow: Int = 5): Seq[NewsHomeItem] = {
      val newsItems = newsHomeCollection.find().limit(numberOfNewsItemsToShow).sort(MongoDBObject("posted" -> -1))
      println("Size is : " + newsItems.size)
      val newsItemsToShow = for (item <- newsItems) yield {
        val id = item.get("_id").asInstanceOf[ObjectId]
        val postedValue = item.get("posted").asInstanceOf[Date]
        val postedValueLocalDate = new LocalDate(postedValue)
        val newsDetails = item.get("newsDetails").asInstanceOf[BasicDBList]
        val lines = for (newsLine <- newsDetails) yield {
          newsLine.toString
        }
        println("The news item is : " + item)
        NewsHomeItem(id, postedValueLocalDate, lines)
      }
      println(" To Show : " + newsItems.underlying)
      newsItemsToShow.toSeq
    }

  }

  class NewsHomeUpdaterMongoDb(val collection: MongoCollection) extends NewsHomeUpdater {
    def save(newsItem: NewsHomeItem) {
    }
  }

}
