package models

import play.api.data.Form

/**
 * Created by Wojtek on 16/05/15.
 */
case class CheckInEvent(userId: Option[String], timestamp: Long, clubId: String) {
}

