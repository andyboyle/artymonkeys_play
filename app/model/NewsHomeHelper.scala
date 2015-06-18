package model

import controllers.ApplicationCake
import org.joda.time.{LocalDateTime, LocalDate}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{AnyContent, Request}

object NewsHomeHelper
{

  val newNewsForm = Form(
    tuple(
      "newnewsitemheadline" -> text,
      "newnewsitemtext" -> text
    )
  )

  def getLastNewsItems: Seq[NewsHomeItem] =
  {
    ApplicationCake.newsHomeService.getLastNewsItems(5)
  }

  def addNewsItem(implicit request: Request[AnyContent]) =
  {
    val (newsHeadline, newsDetail) = newNewsForm.bindFromRequest.get
    val newsDetailSeq = newsDetail.split("\\r\\n").toSeq
    val newsItem: NewsHomeItem = NewsHomeItem(null, LocalDateTime.now() ,newsHeadline, newsDetailSeq)
    ApplicationCake.newsHomeService.save(newsItem)
  }
}
