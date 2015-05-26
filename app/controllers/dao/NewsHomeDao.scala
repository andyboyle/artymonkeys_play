package controllers.dao

import com.mongodb.casbah.Imports._
import controllers.model.NewsHomeItem

class NewsHomeDao {

  val newsHomeCollection = MongoConnection()("artymonkeys")("newshome")

  def getLastNewsItems( numberOfNewsItemsToShow : Int = 2 ): Seq[NewsHomeItem] = {
    val newsItems = newsHomeCollection.find().limit(numberOfNewsItemsToShow).sort(MongoDBObject("posted" -> -1))
    println("Size is : " + newsItems.size)
    val newsItemsToShow = for  (item <- newsItems ) yield {
      val postedValue = item.get("posted")
      val newsDetails = item.get("newsDetails")
      println("The news item is : " + item)
      NewsHomeItem(null, null, null)
    }
    println(" To Show : " + newsItems.underlying)
    newsItemsToShow.toSeq
  }

}
