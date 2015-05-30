package models

import play.api.data.Forms._
import play.api.data._
import play.api.libs.json._
import play.api.mvc._
import play.api.libs.functional.syntax._

/**
 * Created by Wojtek on 16/05/15.
 */

object Message {

  case class CheckInEvent(timestamp: Long, clubId: String)

  case class CheckOutEvent(timestamp: Long, clubId: String)

  case class LocationEvent(timestamp: Long, longitude: Double, latitude: Double)

  case class RatingEvent(timestamp: Long, clubId: String, rating: Int)

  case class QrScanEvent(timestamp: Long, clubId: String, payload: String)

}