package models

/**
 * Created by Wojtek on 16/05/15.
 */
case class RatingEvent(userId: Option[String], timestamp: Long, clubId: String, rating: String) {

}
